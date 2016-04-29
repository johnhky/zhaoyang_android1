package com.doctor.sun.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rick on 22/1/2016.
 */
public class TimeUtils {

    public static String getReadableTime(long timeMillis) {
        String result = "";
        long minute = timeMillis / 60000 % 60;
        long hour = timeMillis / 3600000;
        if (hour> 1) {
            result += hour + "小时";
        }
        result += minute + "分钟";
        return result;
    }

    /**
     */
    public static String formatChatMsgShortDate(long timestamp) {
        long localTimestamp, localTime;
        long now = System.currentTimeMillis();

        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        localTimestamp = timestamp + tz.getOffset(timestamp);
        localTime = now + tz.getOffset(now);

        long dayOrd = localTimestamp / 86400000L;
        long nowOrd = localTime / 86400000L;

        if (dayOrd == nowOrd) {
            return new SimpleDateFormat("hh:mm a", Locale.CHINA).format(new Date(timestamp));
        } else {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.CHINA).format(new Date(timestamp));
        }
    }
}
