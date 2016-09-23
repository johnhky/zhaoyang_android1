package com.doctor.sun;

import android.support.test.espresso.IdlingResource;

import com.doctor.sun.ui.activity.BaseFragmentActivity2;

public class MainActivityIdlingResource implements IdlingResource {

    private BaseFragmentActivity2 activity;
    private ResourceCallback callback;

    public MainActivityIdlingResource(BaseFragmentActivity2 activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return activity.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && callback != null;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
} 