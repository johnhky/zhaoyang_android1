package com.doctor.sun.http;

import android.support.annotation.NonNull;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.Config;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * api service 产生器
 * <p/>
 * Created by Tony on 10/22/15.
 */
public class Api {
    public static final String TAG = Api.class.getSimpleName();

    /**
     * api base
     */
    public static final String API_BASE_URL = Config.getString(Constants.BASE_URI,BuildConfig.BASE_URL);

    /**
     * okhttp
     */
    private static OkHttpClient httpClient = getOkHttpClient();

    @NonNull
    private static OkHttpClient getOkHttpClient() {
//        if (BuildConfig.DEV_MODE) {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        } else {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }
        return new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor()).build();
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

}
