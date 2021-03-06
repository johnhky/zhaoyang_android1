package com.doctor.sun.http;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.ConfigImpl;
import com.doctor.sun.util.JacksonUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * api service 产生器
 * <p/>
 * Created by Tony on 10/22/15.
 */
public class ApiMock {
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
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        if (BuildConfig.DEV_MODE) {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .addInterceptor(new TokenInterceptor()).build();
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
            String userAgent = "app/" + "Junit" + " (android; " + Build.VERSION.RELEASE + "; " + Build.VERSION.SDK_INT + ")";
            // 1.0.0
            String token = ConfigImpl.getInstance().getToken();
            if (token == null) {
                token = "";
            }
            Request request = chain
                    .request()
                    .newBuilder()
                    .addHeader("User-Agent", userAgent)
                    .addHeader("appVersion", BuildConfig.VERSION_NAME)
                    .addHeader("token", token)
                    .addHeader("from", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("client", "android")
                    .build();

            return chain.proceed(request);
        }
    }
}
