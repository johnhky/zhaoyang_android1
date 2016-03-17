package com.doctor.sun.util;

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
}
