package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;

/**
 * Created by rick on 3/5/2016.
 */
public class VoipCallUtil {
    public static final String TAG = VoipCallUtil.class.getSimpleName();
    private static PowerManager.WakeLock wakeLock;

    public static void enableScreenSensor(Context context) {

        int field = 0x00000020;
        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        PowerManager powerManager = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
        wakeLock = getWakeLock(field, powerManager);


        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    public static void disableScreenSensor(Context context) {
        int field = 0x00000020;
        try {
            // Yeah, this is hidden field.
            field = PowerManager.class.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
        }

        PowerManager powerManager = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
        wakeLock = getWakeLock(field, powerManager);

        if (wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
    }

    private static PowerManager.WakeLock getWakeLock(int field, PowerManager powerManager) {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(field, TAG);
        }
        return wakeLock;
    }

}
