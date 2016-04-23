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
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rick on 19/3/2016.
 */
public class UpdateUtil {
    public static final String TAG = UpdateUtil.class.getSimpleName();
    private static final long INTERVAL = 7200000;
    public static final String APK_PATH = "newVersion.apk";
    private static long lastCheckTime = 0;
    private static MaterialDialog dialog;

    public static void checkUpdate(final Activity context) {
        final ToolModule api = Api.of(ToolModule.class);
        if (lastCheckTime + INTERVAL > System.currentTimeMillis()) {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
            return;
        } else {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
        }

        final String versionName = BuildConfig.VERSION_NAME.replace("_dev", "");
        Call<ApiDTO<Version>> getVersionCall = api.getAppVersion("android", versionName);
        getVersionCall.enqueue(new Callback<ApiDTO<Version>>() {
            @Override
            public void onResponse(Response<ApiDTO<Version>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final Version data = response.body().getData();
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
                        builder.onPositive(callback);
                        builder.positiveText("马上更新");
                        if (forceUpdate) {
                            dialog = builder.content("昭阳医生已经发布了最新版本，更新后才可以使用哦！").show();
                        } else if (newVersion > Double.valueOf(versionName)) {
                            builder.negativeText("稍后提醒我");
                            dialog = builder.content("昭阳医生已经发布了最新版本，更新后会有更好的体验哦！").show();
                        }
                    }
                    lastCheckTime = System.currentTimeMillis();
                } else {
                    Log.e(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    public static void reset() {
        lastCheckTime = 0;
    }


    public static void downLoadFile(String from, final File to, final Runnable callback) {
        Call<ResponseBody> downloadCall = Api.of(ToolModule.class).downloadFile(from);
        downloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Response<ResponseBody> response, Retrofit retrofit) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File file = to;
                        FileOutputStream os;
                        InputStream is;
                        try {
                            file.createNewFile();
                            is = response.body().byteStream();
                            os = new FileOutputStream(file);

                            int totalLength = (int) response.body().contentLength();
                            int totalRead = 0;

                            int loopCounter = 0;
                            int read;
                            byte[] buffer = new byte[32768];
                            EventHub.post(new ProgressEvent(0, totalLength));
                            while ((read = is.read(buffer)) > 0) {
                                os.write(buffer, 0, read);
                                totalRead += read;
                                if (loopCounter % 300 == 0) {
                                    EventHub.post(new ProgressEvent(totalRead, totalLength));
                                }
                                loopCounter += 1;
                            }
                            EventHub.post(new ProgressEvent(totalLength, totalLength));
                            os.close();
                            is.close();
                            Tasks.runOnUiThread(callback);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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
            NotificationUtil.onFinishDownloadNewVersion(file);
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
            File to = new File(Config.getDataPath(), APK_PATH);
            downLoadFile(data.getDownloadUrl(), to, new InstallApkCallback(to, this));
        }

        @Subscribe
        public void onProgressEvent(ProgressEvent event) {
            NotificationUtil.showNotification(event.getTotalRead(), event.getTotalLength());
        }

        public void unregisterThis() {
            EventHub.unregister(this);
        }
    }
}
