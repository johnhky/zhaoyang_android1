package com.doctor.sun.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Version;
import com.doctor.sun.module.ToolModule;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.ganguo.library.Config;
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
    private static long lastCheckTime = 0;

    public static void checkUpdate(final ToolModule api) {
        if (lastCheckTime + INTERVAL > System.currentTimeMillis()) {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
            return;
        } else {
            Log.e(TAG, "checkUpdate: " + lastCheckTime);
        }

        Call<ApiDTO<Version>> getVersionCall = api.getAppVersion("android", BuildConfig.VERSION_NAME);
        getVersionCall.enqueue(new Callback<ApiDTO<Version>>() {
            @Override
            public void onResponse(Response<ApiDTO<Version>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Version data = response.body().getData();
                    boolean forceUpdate;
                    double newVersion;
                    if (data == null) {
                        forceUpdate = false;
                        newVersion = 0;
                    } else {
                        forceUpdate = data.getForceUpdate();
                        newVersion = data.getNowVersion();
                    }

                    if (forceUpdate) {
                        Log.e(TAG, "onResponse: " + data.getDownloadUrl());
                        downLoadFile(api, data.getDownloadUrl());
                    } else if (newVersion > Double.valueOf(BuildConfig.VERSION_NAME)) {
                        //TODO 弹
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

    public static void reset(){
        lastCheckTime = 0;
    }

    private static void downLoadFile(ToolModule api, String path) {
        Call<ResponseBody> downloadCall = api.downloadFile(path);
        downloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Response<ResponseBody> response, Retrofit retrofit) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File file = new File(Config.getDataPath(), "newVersion.apk");
                        FileOutputStream os;
                        InputStream is;
                        try {
                            file.createNewFile();
                            is = response.body().byteStream();
                            os = new FileOutputStream(file);

                            int totalLength = (int) response.body().contentLength();
                            int totalRead = 0;

                            NotificationUtil.showNotification(totalRead, totalLength);
                            int read = 0;
                            byte[] buffer = new byte[32768];
                            while ((read = is.read(buffer)) > 0) {
                                os.write(buffer, 0, read);
                                totalRead += read;
                            }
                            os.close();
                            is.close();
                            Tasks.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NotificationUtil.showFinishDownloadNotification(file);
                                    installPackage(AppContext.me(), file.getAbsolutePath());
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
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
}
