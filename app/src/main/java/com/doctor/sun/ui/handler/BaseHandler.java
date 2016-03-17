package com.doctor.sun.ui.handler;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by rick on 11/17/15.
 */
public class BaseHandler {
    private final WeakReference<Activity> context;

    public BaseHandler(Activity context) {
        this.context = new WeakReference<>(context);
    }



    protected Activity getContext() {
        return context.get();
    }
}
