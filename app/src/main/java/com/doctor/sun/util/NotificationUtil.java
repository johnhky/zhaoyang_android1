package com.doctor.sun.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.handler.SettingHandler;

import java.io.File;

/**
 * Created by rick on 1/4/2016.
 */
public class NotificationUtil {

    public static final int NEW_MSG = 100;
    public static final int UPLOAD = 101;

    public static boolean isAppInForeGround = true;


    public static void showNotification(TextMsg msg1) {
        if (isAppInForeGround) {
            return;
        }
        if (!SettingHandler.isEnablePush()) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());

        if (msg1.getItemLayoutId() == R.layout.msg_prescription_list) {
            return;
        }
        builder.setContentTitle("昭阳医生新消息");
        builder.setContentText(msg1.getBody());
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLights(Color.GREEN, 1000, 3000);
        Intent i;
        if (Settings.isDoctor()) {
            i = ConsultingActivity.makeIntent(AppContext.me());
        } else {
            i = PConsultingActivity.makeIntent(AppContext.me());
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        long[] longs = new long[]{0, 300, 150, 300};
        builder.setVibrate(longs);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }


    public static void showNotification(int progress, int totalLength) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("正在下载");
        builder.setContentTitle("昭阳医生");
        builder.setLights(Color.GREEN, 1000, 3000);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setAutoCancel(true);
        builder.setProgress(totalLength, progress, false);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }

    public static void showUploadProgress(int progress, int totalLength) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("正在上传文件消息附件");
        builder.setContentTitle("昭阳医生");
        builder.setLights(Color.GREEN, 1000, 3000);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setAutoCancel(true);
        builder.setProgress(totalLength, progress, false);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(UPLOAD, notification);
    }

    public static void cancelUploadMsg() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("附件发送成功");
        builder.setContentTitle("昭阳医生");
        builder.setLights(Color.GREEN, 1000, 3000);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(UPLOAD, notification);
    }

    public static void onFinishDownloadNewVersion(File file) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("新版本下载已完成,请点击安装");
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

    public static void onFinishDownloadFile(Intent i) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppContext.me());
        builder.setContentText("下载已完成,点击打开文件");
        builder.setContentTitle("昭阳医生");
        builder.setSmallIcon(R.drawable.ic_notification);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), NEW_MSG, i, 0);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AppContext.me());
        managerCompat.notify(NEW_MSG, notification);
    }
}
