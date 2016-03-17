package com.doctor.sun.http.callback;

import com.doctor.sun.dto.ApiDTO;

/**
 * Created by rick on 22/1/2016.
 */
public abstract class SimpleCallback<T> extends ApiCallback<T> {

    @Override
    protected void handleApi(ApiDTO<T> body) {
        handleResponse(null);
    }
}
