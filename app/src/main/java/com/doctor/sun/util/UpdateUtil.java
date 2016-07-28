package com.doctor.sun.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Version;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ToolModule;
import com.squareup.otto.Subscribe;

import java.io.File;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 19/3/2016.
 */
public class UpdateUtil {
    public static final String TAG = UpdateUtil.class.getSimpleName();
    public static int requestCount = 0;
    public static final int STORE_REQUEST = 100;
    public static final String[] PERMISSIONS = new String[]{"android.permission.READ_EXTERNAL_STORAGE", Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final long INTERVAL = 7200000;
    public static final String APK_PATH = "newVersion.apk";
    public static final String NEW_VERSION = "NEW_VERSION" + BuildConfig.VERSION_CODE;
    private static long lastCheckTime = 0;
    private static boolean isHostActivityPaused = false;

    private static onNoNewVersion noNewVersion;


    public static void checkUpdate(final Activity context) {
        if (checkAndRequestPermission(context)) return;

        final ToolModule api = Api.of(ToolModule.class);
        final String myVersion = String.valueOf(BuildConfig.VERSION_CODE);
        if (lastCheckTime + INTERVAL > System.currentTimeMillis()) {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
            String json = Config.getString(NEW_VERSION);
            if (json != null) {
                Version serverVersion = JacksonUtils.fromJson(json, Version.class);
                handleNewVersion(context, serverVersion, myVersion);
                return;
            }
        } else {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
        }

        Call<ApiDTO<Version>> getVersionCall = api.getAppVersion("android", myVersion);
        getVersionCall.enqueue(new Callback<ApiDTO<Version>>() {
            @Override
            public void onResponse(Call<ApiDTO<Version>> call, Response<ApiDTO<Version>> response) {
                if (response.isSuccessful()) {
                    final Version data = response.body().getData();
                    Config.putString(NEW_VERSION, JacksonUtils.toJson(data));
                    handleNewVersion(context, data, myVersion);
                } else {
                    Log.e(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: ");

            }
        });

    }

    private static boolean checkAndRequestPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtil.hasSelfPermission(context, PERMISSIONS)) {
                if (requestCount < 2) {
                    context.requestPermissions(PERMISSIONS, STORE_REQUEST);
                    requestCount += 1;
                }
                return true;
            }
        }
        return false;
    }

    private static void handleNewVersion(Activity context, Version data, String versionName) {
        boolean forceUpdate;
        double newVersion;
        if (data == null) {
            forceUpdate = false;
            newVersion = 0;
        } else {
            forceUpdate = data.getForceUpdate();
            newVersion = data.getNowVersion();
        }

        if (context.getWindow().isActive() && !isHostActivityPaused) {
            final DownloadNewVersionCallback callback = new DownloadNewVersionCallback(data);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.canceledOnTouchOutside(false);
            builder.cancelable(false);
            builder.onPositive(callback);
            builder.positiveText("马上更新");
            if (forceUpdate) {
                builder.content("昭阳医生已经发布了最新版本，更新后才可以使用哦！").show();
            } else if (newVersion > Double.valueOf(versionName)) {
                builder.negativeText("稍后提醒我");
                builder.content("昭阳医生已经发布了最新版本，更新后会有更好的体验哦！").show();
            } else {
                if (noNewVersion != null) {
                    noNewVersion.onNoNewVersion();
                    noNewVersion = null;
                }
            }
        }
        lastCheckTime = System.currentTimeMillis();
    }

    public static void reset() {
        lastCheckTime = 0;
        isHostActivityPaused = false;
    }

    public static void onPause() {
        isHostActivityPaused = true;
    }


    public static void installPackage(Context context, String filePath) {
        Intent intent = getInstallIntent(filePath);
        if (intent == null) return;
        context.startActivity(intent);
    }

    @Nullable
    public static Intent getInstallIntent(String filePath) {
        File installFile = new File(filePath);
        if (!installFile.exists()) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = FileProvider.getUriForFile(AppContext.me(), BuildConfig.FILE_PROVIDER, new File(filePath));
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    private static class InstallApkCallback implements Try {
        private final File file;
        private final DownloadNewVersionCallback callback;

        public InstallApkCallback(File file, DownloadNewVersionCallback callback) {
            this.file = file;
            this.callback = callback;
        }

        @Override
        public void success() {
            callback.unregisterThis();
            installPackage(AppContext.me(), file.getAbsolutePath());
        }

        @Override
        public void fail() {

        }
    }

    private static class DownloadNewVersionCallback implements MaterialDialog.SingleButtonCallback {
        private final Version data;

        public DownloadNewVersionCallback(Version data) {
            this.data = data;
        }

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            EventHub.register(this);
            File to = new File(Config.getTempPath(), APK_PATH);
            final InstallApkCallback callback = new InstallApkCallback(to, this);
            if (!isDownloaded(data.getMd5(), to)) {
                DownloadUtil.downLoadFile(data.getDownloadUrl(), to, callback);
            } else {
                callback.success();
            }
        }

        private boolean isDownloaded(String md5, File to) {
            if (!to.exists()) {
                return false;
            }
            try {
                return md5.equals(MD5.getMessageDigest(to));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Subscribe
        public void onProgressEvent(ProgressEvent event) {
            if (event.getTotalLength() == event.getTotalRead()) {
                NotificationUtil.onFinishDownloadNewVersion(new File(Config.getTempPath(), APK_PATH));
            } else {
                NotificationUtil.showNotification(event.getTotalRead(), event.getTotalLength());
            }
        }

        public void unregisterThis() {
            EventHub.unregister(this);
        }
    }

    public static void setNoNewVersion(onNoNewVersion noNewVersion) {
        UpdateUtil.noNewVersion = noNewVersion;
    }

    public interface onNoNewVersion {
        void onNoNewVersion();
    }
}
