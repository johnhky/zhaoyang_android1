package com.doctor.sun.event;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.doctor.sun.im.Messenger;
import com.doctor.sun.ui.activity.LoginActivity;
import com.squareup.otto.Subscribe;

import io.ganguo.library.AppManager;
import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 11/27/15.
 */
public class OnTokenExpireEvent implements Event {
    private Context context;

    public OnTokenExpireEvent() {
    }

    public OnTokenExpireEvent(Context context) {
        this.context = context;
    }

    /**
     * Token 过期
     *
     * @param event
     */
    @Subscribe
    public void onAuthEvent(OnTokenExpireEvent event) {
        Toast.makeText(context, "帐号登录过期,请重新登录", Toast.LENGTH_LONG).show();
        Messenger.getInstance().logout();

        Intent intent = LoginActivity.makeIntent(context);
        context.startActivity(intent);
        AppManager.finishAllActivity();
    }
}
