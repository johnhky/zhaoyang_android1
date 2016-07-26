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


    private String stringToFormat = "";
    private long remainTime;
    private boolean isRunning = false;

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
        if (isRunning) {
            return;
        }
        isRunning = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (stringToFormat.equals("")) {
                    stringToFormat = getText().toString();
                }
                setText(String.format(Locale.CHINA, stringToFormat, getReadableTime(remainTime)));
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
        long second = (timeMillis / 1000) % 60;
        long minute = timeMillis / 60000 % 60;
        long hour = timeMillis / 3600000;
        if (hour >= 1) {
            result += String.format(Locale.CHINA, "%02d小时", hour);
        }
        result += String.format(Locale.CHINA, "%02d分", minute);
        result += String.format(Locale.CHINA, "%02d秒", second);
        return result;
    }
}
