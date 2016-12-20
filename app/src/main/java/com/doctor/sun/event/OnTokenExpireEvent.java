package com.doctor.sun.event;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.ui.activity.LoginActivity;
import com.doctor.sun.ui.handler.SettingHandler;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.Event;
import io.realm.Realm;

/**
 * Created by rick on 11/27/15.
 */
public class OnTokenExpireEvent implements Event {
    public static final int ONE_MINUTES = 60000;
    private static long lastEventTime = 0;

    public OnTokenExpireEvent() {
    }


    /**
     * Token 过期
     *
     * @param event
     */
    @Subscribe
    public void onAuthEvent(OnTokenExpireEvent event) {
        if (System.currentTimeMillis() - lastEventTime > ONE_MINUTES) {
            MobclickAgent.onProfileSignOff();
            Toast.makeText(AppContext.me(), "帐号登录过期,请重新登录", Toast.LENGTH_LONG).show();
            IMManager.getInstance().logout();

            SettingHandler.clearUserData();
            Intent intent = LoginActivity.makeIntent(AppContext.me());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AppContext.me().startActivity(intent);
            AppManager.finishAllActivity();
            lastEventTime = System.currentTimeMillis();
            JPushInterface.setAlias(AppContext.me(), "", new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });
        }
    }
}
