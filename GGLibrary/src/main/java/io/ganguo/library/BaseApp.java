package io.ganguo.library;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import io.ganguo.library.core.event.EventHub;

/**
 * Application - 基类
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class BaseApp extends Application {

    private static BaseApp APP;

    /**
     * 单例
     *
     * @return
     */
    public static <T extends BaseApp> T me() {
        return (T) APP;
    }

    /**
     * 应用启动
     */
    @Override
    public void onCreate() {
        super.onCreate();

        APP = this;

        // register
        EventHub.register(this);
        //TODO: uncomment this to print the log message about the lifecycle of activities.
        registerActivityLifecycleCallbacks(new LifecycleLoggingCallbacks());
    }

    /**
     * 应用销毁
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        // unregister
        EventHub.unregister(this);
    }

    private final class LifecycleLoggingCallbacks implements ActivityLifecycleCallbacks {
        private final String TAG = "Lifecycle";

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, activity.getLocalClassName() + " onCreated");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, activity.getLocalClassName() + " onStarted");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, activity.getLocalClassName() + " onResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, activity.getLocalClassName() + " onPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, activity.getLocalClassName() + " onStopped");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, activity.getLocalClassName() + " onSaveInstanceState");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, activity.getLocalClassName() + " onDestroyed");
        }
    }
}
