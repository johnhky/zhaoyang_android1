package com.doctor.sun;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.module.AuthModule;
import com.squareup.otto.Subscribe;
import com.yuntongxun.ecsdk.ECDevice;

import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.OnExitEvent;
import io.ganguo.opensdk.OpenSDK;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * App 上下文环境
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class AppContext extends BaseApp {
    public static final String TAG = AppContext.class.getSimpleName();
    public static final String COM_DOCTOR_SUN = "com.doctor.sun";
    private static int userType = -1;
    private static boolean isInitialized;

    @Override
    public void onCreate() {
        super.onCreate();

        AppEnv.init(this);
        // init libs
        String processName = getProcessName(Process.myPid());
        if (processName.equals(COM_DOCTOR_SUN)) {
            if (AppEnv.DEV_MODE) {
                OpenSDK.initStage(this);
            } else {
                OpenSDK.initProduct(this);
            }
        }
        initMessenger();


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm dynamicRealm, long l, long l1) {
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static void initMessenger() {
        if (!ECDevice.isInitialized()) {
            ECDevice.initial(AppContext.me(), new ECDevice.InitListener() {
                @Override
                public void onInitialized() {
                    // SDK已经初始化成功
                    Log.e(TAG, "onInitialized: ");
                    isInitialized = true;
                }

                @Override
                public void onError(Exception exception) {
                    Log.e(TAG, "onError: ");
                    isInitialized = false;
                    exception.printStackTrace();
                    // SDK 初始化失败,可能有如下原因造成
                    // 1、可能SDK已经处于初始化状态
                    // 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、
                    //    或者未配置服务属性android:exported="false";
                    // 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持
                    //    Android Build.VERSION.SDK_INT 以及以上版本）
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 应用退出事件
     *
     * @param event
     */
    @Subscribe
    public void onExitEvent(OnExitEvent event) {

    }

    public static boolean isDoctor() {
        if (userType == -1) {
            userType = Config.getInt(Constants.USER_TYPE, -1);
        }
        return userType != AuthModule.PATIENT_TYPE;
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public String getProcessName(int pid) {
        ActivityManager mActivityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
