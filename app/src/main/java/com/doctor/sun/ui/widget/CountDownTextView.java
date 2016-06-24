package com.doctor.sun.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by rick on 26/4/2016.
 */
public class CountDownTextView extends TextView {
    public static final String TAG = CountDownTextView.class.getSimpleName();
    public static final int ONE_SECOND = 1000;


    private long remainTime;

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTime(long time) {
        Log.d(TAG, "setTime() called with: " + "time = [" + time + "]");
        long currentTimeMillis = System.currentTimeMillis();
        if (time > currentTimeMillis) {
            setVisibility(VISIBLE);
            remainTime = (time - currentTimeMillis);
            countDown();
        } else {
            setVisibility(GONE);
        }
    }

    public void countDown() {
        Log.d(TAG, "countDown() called with: " + "");
        post(new Runnable() {
            @Override
            public void run() {
                setText(String.format(Locale.CHINA, "剩余就诊时长%s", getReadableTime(remainTime)));
                remainTime -= ONE_SECOND;
                if (remainTime > 0) {
                    postDelayed(this, ONE_SECOND);
                } else {
                    removeCallbacks(this);
                    setVisibility(GONE);
                }
            }
        });
    }

    public static String getReadableTime(long timeMillis) {
        String result = "";
        long second = (timeMillis / 10000) % 60;
        long minute = timeMillis / 60000 % 60;
        long hour = timeMillis / 3600000;
        if (hour > 1) {
            result += hour + "小时";
        }
        result += minute + "分";
        result += second + "秒";
        return result;
    }
}
