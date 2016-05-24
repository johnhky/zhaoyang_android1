package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    public static final long INTERVAL = 7200000;
    public static final String APK_PATH = "newVersion.apk";
    public static final String NEWVERSION = "NEWVERSION";
    private static long lastCheckTime = 0;
    private static MaterialDialog dialog;

    private static onNoNewVersion noNewVersion;

    public static void checkUpdate(final Activity context) {
        final ToolModule api = Api.of(ToolModule.class);
        final String myVersion = String.valueOf(BuildConfig.VERSION_CODE);
        if (lastCheckTime + INTERVAL > System.currentTimeMillis()) {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
            String json = Config.getString(NEWVERSION);
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
                    Config.putString(NEWVERSION, JacksonUtils.toJson(data));
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

        if (context.getWindow().isActive()) {
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            final DownloadNewVersionCallback callback = new DownloadNewVersionCallback(data);
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
            builder.canceledOnTouchOutside(false);
            builder.cancelable(false);
            builder.onPositive(callback);
            builder.positiveText("马上更新");
            if (forceUpdate) {
                dialog = builder.content("昭阳医生已经发布了最新版本，更新后才可以使用哦！").show();
            } else if (newVersion > Double.valueOf(versionName)) {
                builder.negativeText("稍后提醒我");
                dialog = builder.content("昭阳医生已经发布了最新版本，更新后会有更好的体验哦！").show();
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
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        return intent;
    }

    private static class InstallApkCallback implements Runnable {
        private final File file;
        private final DownloadNewVersionCallback callback;

        public InstallApkCallback(File file, DownloadNewVersionCallback callback) {
            this.file = file;
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.unregisterThis();
            installPackage(AppContext.me(), file.getAbsolutePath());
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
            InstallApkCallback callback = new InstallApkCallback(to, this);
            if (!isDownloaded(data.getMd5(), to)) {
                DownloadUtil.downLoadFile(data.getDownloadUrl(), to, callback);
            } else {
                callback.run();
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
