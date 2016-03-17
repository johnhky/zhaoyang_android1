package com.doctor.sun.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.util.JacksonUtils;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import io.ganguo.library.Config;
import io.ganguo.library.util.Systems;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * api service 产生器
 * <p>
 * Created by Tony on 10/22/15.
 */
public class Api {
    public static final String TAG = Api.class.getSimpleName();

    /**
     * api base
     */
    public static final String API_BASE_URL = BuildConfig.BASE_URL;

    /**
     * okhttp
     */
    private static OkHttpClient httpClient = getOkHttpClient();

    @NonNull
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.interceptors().add(new TokenInterceptor());
        return okHttpClient;
    }


    /**
     * retrofit builder
     */
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create(JacksonUtils.getInstance()));

    /**
     * 创建一个api service
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S of(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    private static class TokenInterceptor implements Interceptor {

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

            Request request = chain
                    .request()
                    .newBuilder()
                    .addHeader("User-Agent", userAgent)
                    .addHeader("version", version)
                    .addHeader("token", token)
                    .addHeader("from", "android")
                    .build();
            Log.e(TAG, request.method() + " " + request.urlString() + " token " + token);
            return chain.proceed(request);
        }
    }
}
