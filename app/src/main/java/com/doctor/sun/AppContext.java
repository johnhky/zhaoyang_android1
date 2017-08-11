package com.doctor.sun;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.doctor.sun.bean.Province;
import com.doctor.sun.im.AVChatHandler;
import com.doctor.sun.im.CustomAttachParser;
import com.doctor.sun.im.observer.AttachmentProgressObserver;
import com.doctor.sun.im.observer.MsgStatusObserver;
import com.doctor.sun.im.observer.ReceiveMsgObserver;
import com.doctor.sun.im.observer.RevokeMessageObserver;
import com.doctor.sun.immutables.Appointment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.ganguo.library.BaseApp;
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
    public static final int NEW_VERSION = 9;
    public static int chat_num = 0;
    public static Appointment data;
    public static Appointment appointment;
    public static AppContext instance;
    public static Appointment keepState;
    public static List<String>records;
    public static int position;
    public static int type = 0;
    public static Appointment editAppointment;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppEnv.init(this);
        // init libs
        String processName = getProcessName(this);


        //关闭自动下载attachment
        SDKOptions sdkOptions = new SDKOptions();
        sdkOptions.preloadAttach = false;
        NIMClient.init(this, null, sdkOptions);

        if (processName.equals(getPackageName())) {
            if (AppEnv.DEV_MODE) {
                OpenSDK.initStage(this);
            } else {
                OpenSDK.initProduct(this);
            }

            registerMsgObserver();
            AVChatHandler.getInstance().enableAVChat();

            JPushInterface.init(this);
        }

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
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
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);

        MobclickAgent.UMAnalyticsConfig umConfig =
                new MobclickAgent.UMAnalyticsConfig(this, BuildConfig.UM_KEY,
                        BuildConfig.FLAVOR, MobclickAgent.EScenarioType.E_UM_NORMAL, true);
        MobclickAgent.startWithConfigure(umConfig);
        MobclickAgent.setDebugMode(BuildConfig.DEV_MODE);
        MobclickAgent.openActivityDurationTrack(false);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    private void registerMsgObserver() {
        CustomAttachParser msgAttachmentParser = new CustomAttachParser();
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(msgAttachmentParser);

        MsgStatusObserver msgStatusObserver = new MsgStatusObserver();
        ReceiveMsgObserver receiveMsgObserver = new ReceiveMsgObserver();
        AttachmentProgressObserver progressObserver = new AttachmentProgressObserver();
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        RevokeMessageObserver revokeMessageObserver = new RevokeMessageObserver();
        service.observeReceiveMessage(receiveMsgObserver, true);
        service.observeMsgStatus(msgStatusObserver, true);
        service.observeAttachmentProgress(progressObserver, true);
        service.observeRevokeMessage(revokeMessageObserver,true);
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
            if (oldVersion <= 8) {
                realm.deleteAll();
            }
        }
    }

    public int getChat_num() {
        return chat_num;
    }

    public void setChat_num(int chat_num) {
        this.chat_num = chat_num;
    }

    public static Appointment getData() {
        return data;
    }

    public static void setData(Appointment data) {
        AppContext.data = data;
    }

    public static int getType() {
        return type;
    }

    public static void setType(int type) {
        AppContext.type = type;
    }

    public static Appointment getAppointment() {
        return appointment;
    }

    public static void setAppointment(Appointment appointment) {
        AppContext.appointment = appointment;
    }

    public static Appointment getKeepState() {
        return keepState;
    }

    public static void setKeepState(Appointment keepState) {
        AppContext.keepState = keepState;
    }

    public static List<String> getRecords() {
        return records;
    }

    public static void setRecords(List<String> records) {
        AppContext.records = records;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int position) {
        AppContext.position = position;
    }

    public static Appointment getEditAppointment() {
        return editAppointment;
    }

    public static void setEditAppointment(Appointment editAppointment) {
        AppContext.editAppointment = editAppointment;
    }

}
