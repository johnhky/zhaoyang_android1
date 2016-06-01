package com.doctor.sun;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.bean.Province;
import com.doctor.sun.im.AVChatHandler;
import com.doctor.sun.im.observer.AttachmentProgressObserver;
import com.doctor.sun.im.observer.MsgStatusObserver;
import com.doctor.sun.im.observer.ReceiveMsgObserver;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.util.CrashHandler;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.squareup.otto.Subscribe;
import com.yuntongxun.ecsdk.ECDevice;

import java.io.IOException;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;
import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.OnExitEvent;
import io.ganguo.opensdk.OpenSDK;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * App 上下文环境
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class AppContext extends BaseApp {
    public static final String TAG = AppContext.class.getSimpleName();
    public static final int NEW_VERSION = 6;
    private static int userType = -1;
    private static boolean isInitialized;

    @Override
    public void onCreate() {
        super.onCreate();

        AppEnv.init(this);
        // init libs
        String processName = getProcessName(this);

        initMessenger();

        //关闭自动下载attachment
        SDKOptions sdkOptions = new SDKOptions();
        sdkOptions.preloadAttach = false;
        NIMClient.init(this, null, sdkOptions);

        if (processName.equals(getPackageName())) {
            if (AppEnv.DEV_MODE) {
                OpenSDK.initStage(this);
            } else {
                CrashHandler.getInstance().init(this);
                OpenSDK.initProduct(this);
            }

            registerMsgObserver();
            AVChatHandler.getInstance().enableAVChat();

            JPushInterface.init(this);
        }

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(NEW_VERSION)
                .migration(new DoctorSunMigration())
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        InputStream is = getResources().openRawResource(R.raw.provinces_cities);
                        try {
                            realm.createOrUpdateAllFromJson(Province.class, is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
    }

    private void registerMsgObserver() {
        MsgStatusObserver msgStatusObserver = new MsgStatusObserver();
        ReceiveMsgObserver receiveMsgObserver = new ReceiveMsgObserver();
        AttachmentProgressObserver progressObserver = new AttachmentProgressObserver();


        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMsgObserver, true);
        service.observeMsgStatus(msgStatusObserver, true);
        service.observeAttachmentProgress(progressObserver, true);
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
        userType = Config.getInt(Constants.USER_TYPE, -1);
        return userType != AuthModule.PATIENT_TYPE;
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static class DoctorSunMigration implements RealmMigration {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();
            RealmObjectSchema textMsg = schema.get("TextMsg");
            RealmObjectSchema doctorIndex = schema.get("DoctorIndex");
            if (oldVersion < 1) {
                textMsg.addField("finished", boolean.class);
                oldVersion++;
            }
            if (oldVersion < 2) {
                textMsg.addField("imageWidth", int.class)
                        .addField("imageHeight", int.class);
                oldVersion++;
            }
            if (oldVersion < 3) {
                if (!textMsg.hasField("duration")) {
                    textMsg.addField("duration", int.class);
                }
                oldVersion++;
            }
            if (oldVersion < 4) {
                if (!textMsg.hasField("haveListen")) {
                    textMsg.addField("haveListen", boolean.class);
                }
                oldVersion++;
            }
            if (oldVersion < 5) {
                if (!doctorIndex.hasField("followUpNum")) {
                    doctorIndex.addField("followUpNum", int.class);
                }
                oldVersion++;
            }
        }
    }
}
