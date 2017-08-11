package com.doctor.sun.http;

import android.os.Build;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;

import java.io.IOException;

import io.ganguo.library.Config;
import io.ganguo.library.util.Systems;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rick on 5/9/2016.
 */
class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // app/1.0_dev (android; 4.4.4; 19)
        String userAgent = "app/" + Systems.getVersionName(AppContext.me()) + " (android; " + Build.VERSION.RELEASE + "; " + Build.VERSION.SDK_INT + ")";
        // 1.0.0
        String version = Systems.getVersionName(AppContext.me()) + "";
        String token = Config.getString(Constants.TOKEN);
        if (token == null) {
            token = "";
        }
        String versionName = "";
        if (BuildConfig.USER_TYPE.equals("doctor")) {
            versionName = "2.1.0";
        } else {
            versionName = "3.1.0";
        }
        Request request = chain
                .request()
                .newBuilder()
                .addHeader("User-Agent", userAgent)
                .addHeader("appVersion", version)
                .addHeader("token", token)
                .addHeader("from", "android")
                .addHeader("version", versionName)
                .addHeader("client", "android")
                .addHeader("type", BuildConfig.USER_TYPE)
                .build();
        if (BuildConfig.DEV_MODE) {
            Log.e(Api.TAG, request.method() + " " + request.url() + " token " + token);
        }
        return chain.proceed(request);
    }
}
