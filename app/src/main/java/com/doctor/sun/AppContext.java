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
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;
import io.ganguo.library.BaseApp;
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
    public static final int NEW_VERSION = 7;
    private static boolean isInitialized;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

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

        MobclickAgent.UMAnalyticsConfig umConfig =
                new MobclickAgent.UMAnalyticsConfig(this, BuildConfig.UM_KEY,
                        BuildConfig.FLAVOR, MobclickAgent.EScenarioType.E_UM_NORMAL, true);
        MobclickAgent.startWithConfigure(umConfig);
        MobclickAgent.setDebugMode(BuildConfig.DEV_MODE);
        MobclickAgent.openActivityDurationTrack(false);
    }

    private void registerMsgObserver() {
        CustomAttachParser msgAttachmentParser = new CustomAttachParser();
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(msgAttachmentParser);

        MsgStatusObserver msgStatusObserver = new MsgStatusObserver();
        ReceiveMsgObserver receiveMsgObserver = new ReceiveMsgObserver();
        AttachmentProgressObserver progressObserver = new AttachmentProgressObserver();


        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMsgObserver, true);
        service.observeMsgStatus(msgStatusObserver, true);
        service.observeAttachmentProgress(progressObserver, true);
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
                schema.remove("DoctorIndex");
                oldVersion++;
            }
            if (oldVersion <= 6) {
                RealmObjectSchema pair = schema.create("AttachmentPair");
                pair.addField("key", String.class);
                pair.addField("value", String.class);
                textMsg.addRealmListField("attachment", pair);
                realm.where("TextMsg").findAll().deleteAllFromRealm();
                oldVersion++;
            }
        }
    }
}
