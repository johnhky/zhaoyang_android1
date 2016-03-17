package io.ganguo.library.core.event.extend;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Activity 生命周期
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class ActivityLifecycleAdapter implements Application.ActivityLifecycleCallbacks {

    /**
     * 委托对象
     */
    private Application.ActivityLifecycleCallbacks delegate;

    public void setDelegate(Application.ActivityLifecycleCallbacks delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (this.delegate != null) {
            this.delegate.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (this.delegate != null) {
            this.delegate.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (this.delegate != null) {
            this.delegate.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (this.delegate != null) {
            this.delegate.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (this.delegate != null) {
            this.delegate.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (this.delegate != null) {
            this.delegate.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (this.delegate != null) {
            this.delegate.onActivityDestroyed(activity);
        }
    }
}

