package com.doctor.sun.http;

import com.google.gson.reflect.TypeToken;

import io.ganguo.library.exception.NetworkException;
import io.ganguo.library.util.gson.GsonUtils;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 可选
 * 通用 http callback
 * <p/>
 * Created by Tony on 10/22/15.
 */
public abstract class HttpCallback<T, E> implements Callback<T> {

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if (response.code() < 300) {
            // ok
            onSuccess(response.body());
        } else {
            // error
            try {
                TypeToken type = new TypeToken<E>() {
                };
                E e = GsonUtils.fromJson(response.errorBody().string(), type.getType());
                onError(e);
            } catch (Exception e) {
                onFailure(e);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        onException(new NetworkException(t));
    }

    public abstract void onSuccess(T data);

    public abstract void onError(E error);

    public abstract void onException(NetworkException e);

}
