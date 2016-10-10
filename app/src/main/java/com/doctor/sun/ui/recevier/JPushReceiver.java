package com.doctor.sun.ui.recevier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.JPushExtra;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.util.JacksonUtils;
import com.google.common.base.Strings;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则:
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    public static final String TAG = JPushReceiver.class.getSimpleName();

    public static final String ACTION_ACCEPT_RELATION = "ACCEPT_RELATION";
    public static final String ACTION_FOLLOW_UP_DETAIL = "ACTION_FOLLOW_UP_DETAIL";
    public static final String ACTION_APPOINTMENT_DETAIL = "ACTION_APPOINTMENT_DETAIL";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            onRegisterEvent(bundle);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            onCustomMessageReceived(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            onNotificationReceived(bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            onNotificationOpened(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            onRichPushReceived(bundle);
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            onConnectionChanged(intent);
        } else if (ACTION_APPOINTMENT_DETAIL.equals(intent.getAction())) {
            viewAppointmentDetail(context, intent);
        } else if (ACTION_FOLLOW_UP_DETAIL.equals(intent.getAction())) {
            viewAppointmentDetail(context, intent);
        } else if (ACTION_ACCEPT_RELATION.equals(intent.getAction())) {

        }
    }

    public void cancelNotification(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getIntExtra(Constants.NOTIFICATION_ID, 0));
    }

    public void viewAppointmentDetail(final Context context, Intent intent) {
        String id = intent.getStringExtra(Constants.DATA);
        AppointmentModule api = Api.of(AppointmentModule.class);
        api.appointmentDetail(id).enqueue(new SimpleCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
                response.getHandler().viewDetail(context, 0);
            }
        });
    }

    public void onConnectionChanged(Intent intent) {
    }

    public void onRichPushReceived(Bundle bundle) {
    }

    @Deprecated
    public void onNotificationOpened(Context context, Bundle bundle) {
    }

    public void onNotificationReceived(Bundle bundle) {
        Log.d(TAG, "onNotificationReceived() called");
        int notificationId = getNotificationId(bundle);
        String msg = getAlertMsg(bundle);
        SystemMsg.setLastMsg(msg, notificationId);

        buildNotification(bundle);
    }

    public void onCustomMessageReceived(Context context, Bundle bundle) {
        Log.d(TAG, "onCustomMessageReceived() called");
    }

    public void onRegisterEvent(Bundle bundle) {
    }


    public static void buildNotification(Bundle data) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText(getAlertMsg(data));
        builder.setContentTitle("昭阳医生");
        builder.setSmallIcon(R.drawable.ic_notification);


        NotificationCompat.Action action = buildAction(data);
        if (action != null) {
            builder.addAction(action);
        }

        builder.setGroup(Constants.J_PUSH_MSG);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(getNotificationId(data), notification);
    }

    public static NotificationCompat.Action buildAction(Bundle data) {
        String actionName = "";
        JPushExtra extra = getExtra(data);
        Intent intent = new Intent();
        int notificationId = getNotificationId(data);
        if (!Strings.isNullOrEmpty(extra.applyId)) {
            intent.setAction(ACTION_ACCEPT_RELATION);
            intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            intent.putExtra(Constants.DATA, extra.applyId);
            actionName = "建立随访关系";
        }

        if (!Strings.isNullOrEmpty(extra.followUpId)) {
            intent.setAction(ACTION_FOLLOW_UP_DETAIL);
            intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            intent.putExtra(Constants.DATA, extra.followUpId);
            if (Settings.isDoctor()) {
                actionName = "查看问卷";
            } else {
                actionName = "填写问卷";
            }
        }

        if (!Strings.isNullOrEmpty(extra.appointmentId)) {
            intent.setAction(ACTION_APPOINTMENT_DETAIL);
            intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
            intent.putExtra(Constants.DATA, extra.appointmentId);
            if (Settings.isDoctor()) {
                actionName = "查看问卷";
            } else {
                actionName = "填写问卷";
            }
        }

        if (!Strings.isNullOrEmpty(intent.getAction())) {
            PendingIntent i = PendingIntent.getBroadcast(AppContext.me(), notificationId, intent, 0);
            return new NotificationCompat.Action(0, actionName, i);
        } else {
            return null;
        }
    }


    public static String getAlertMsg(Bundle bundle) {
        return bundle.getString(JPushInterface.EXTRA_ALERT);
    }

    public static int getNotificationId(Bundle bundle) {
        return bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
    }

    private static JPushExtra getExtra(Bundle bundle) {
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        return JacksonUtils.fromJson(extra, JPushExtra.class);
    }
}
