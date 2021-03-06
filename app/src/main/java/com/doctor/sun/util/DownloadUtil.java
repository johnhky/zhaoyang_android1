package com.doctor.sun.util;

import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ToolModule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 9/5/2016.
 */
public class DownloadUtil {
    public static void downLoadFile(final String from, final File to, final Try callback) {
        Call<ResponseBody> downloadCall = Api.of(ToolModule.class).downloadFile(from);
        downloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response==null){
                        return;
                    }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File file = to;
                        FileOutputStream os;
                        InputStream is;
                        try {
                            file.createNewFile();
                            if (null!=response.body()){
                                is = response.body().byteStream();
                                os = new FileOutputStream(file);

                                int totalLength = (int) response.body().contentLength();
                                int totalRead = 0;

                                int loopCounter = 0;
                                int read;
                                byte[] buffer = new byte[32768];
                                EventHub.post(new ProgressEvent(from, 0, totalLength));
                                while ((read = is.read(buffer)) > 0) {
                                    os.write(buffer, 0, read);
                                    totalRead += read;
                                    if (loopCounter % 600 == 0) {
                                        EventHub.post(new ProgressEvent(from, totalRead, totalLength));
                                    }
                                    loopCounter += 1;
                                }
                                EventHub.post(new ProgressEvent(from, totalLength, totalLength));
                                os.close();
                                is.close();
                                Tasks.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.success();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.fail();
                    }
                });
            }
        });
    }
}
