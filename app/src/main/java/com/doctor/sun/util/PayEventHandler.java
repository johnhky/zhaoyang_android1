package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.doctor.sun.AppContext;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.event.PayFailEvent;
import com.doctor.sun.event.PaySuccessEvent;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 12/10/2016.
 */

public class PayEventHandler {

    private Activity activity;

    private PayEventHandler(Activity activity) {
        this.activity = activity;
    }

    @Subscribe
    public void onPaySuccessEvent(PaySuccessEvent e) {
        Context context = AppContext.me();
        Intent intent = PaySuccessActivity.makeIntent(context, e.getData());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.finishAffinity();
        }
    }

    @Subscribe
    public void onPayFailEvent(PayFailEvent e) {
        if (e.getPayAppointment() == IntBoolean.TRUE) {
            Context context = AppContext.me();
            Intent intent = PayFailActivity.makeIntent(context, e.getData(), e.isPayWithWechat());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Context context = AppContext.me();
            Intent intent = PayFailActivity.makeIntent(context, e.getMoney(), e.isPayWithWechat(), e.getExtraField());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.finishAffinity();
        }
    }

    public static PayEventHandler register(Activity activity) {
        PayEventHandler eventHandler = new PayEventHandler(activity);
        EventHub.register(eventHandler);
        return eventHandler;
    }

    public static void unregister(PayEventHandler handler) {
        EventHub.unregister(handler);
    }
}
