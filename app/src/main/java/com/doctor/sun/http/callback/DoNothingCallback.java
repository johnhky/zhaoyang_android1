package com.doctor.sun.http.callback;

import android.util.Log;

/**
 * Created by rick on 11/10/15.
 */
public class DoNothingCallback extends ApiCallback<String> {
    public static final String TAG = DoNothingCallback.class.getSimpleName();
    @Override
    protected void handleResponse(String s) {
        Log.d(TAG, "handleResponse() called with: " + "s = [" + s + "]");
    }
}
