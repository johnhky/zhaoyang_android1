package com.doctor.sun.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;

import java.io.File;

/**
 * Created by rick on 1/4/2016.
 */
public class NotificationUtil {

    public static final int NEW_MSG = 100;

    public static void showNotification(TextMsg msg1) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText(msg1.getBody());
        builder.setContentTitle("昭阳医生新消息");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLights(Color.GREEN, 1000, 3000);
        Intent i;
        if (AppContext.isDoctor()) {
            i = ConsultingActivity.makeIntent(AppContext.me());
        } else {
            i = com.doctor.sun.ui.activity.patient.ConsultingActivity.makeIntent(AppContext.me());
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }


    public static void showNotification(int progress,int totalLength) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("正在下载新版本");
        builder.setContentTitle("昭阳医生");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setAutoCancel(true);
        builder.setProgress(totalLength, progress, true);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }

    public static void showFinishDownloadNotification(File file) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("下载已完成,请点击安装");
        builder.setContentTitle("昭阳医生");
        builder.setSmallIcon(R.drawable.ic_notification);
        Intent i = UpdateUtil.getInstallIntent(file.getAbsolutePath());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }
}
