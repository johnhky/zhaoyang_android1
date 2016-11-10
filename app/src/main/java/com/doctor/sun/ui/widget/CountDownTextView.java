package com.doctor.sun.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.Locale;

import io.ganguo.library.core.event.Event;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 26/4/2016.
 */
public class CountDownTextView extends TextView {
    public static final String TAG = CountDownTextView.class.getSimpleName();
    public static final int ONE_SECOND = 1000;


    private boolean isRunning = false;
    private long remainTime;
    private MyRunnable action;

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
        Log.d(TAG, "countDown() called with: remainTime" + remainTime);
        if (isRunning) {
            return;
        }
        isRunning = true;
        action = new MyRunnable(getText().toString(), remainTime);
        post(action);
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventHub.register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
        EventHub.unregister(this);
    }

    @Subscribe
    public void onTimeoutEvent(CountDownTimeoutEvent e) {
        setVisibility(GONE);
    }

    @Subscribe
    public void onTextChangedEvent(TextChangedEvent e) {
        setText(e.getText());
    }


    private static class MyRunnable implements Runnable {

        private long remainTime;
        private String stringToFormat;

        MyRunnable(String stringToFormat, long remainTime) {
            this.remainTime = remainTime;
            this.stringToFormat = stringToFormat;
        }

        @Override
        public void run() {
            String text = String.format(Locale.CHINA, stringToFormat, getReadableTime(remainTime));
            EventHub.post(new TextChangedEvent(text));
            remainTime -= ONE_SECOND;
            if (remainTime > 0) {
                Tasks.removeRunnable(this);
                Tasks.runOnUiThread(this, ONE_SECOND);
            } else {
                Tasks.removeRunnable(this);
                EventHub.post(new CountDownTimeoutEvent());
            }
        }

    }

    public void stop() {
        remainTime = -1;
        Tasks.removeRunnable(action);
    }

    private static class CountDownTimeoutEvent implements Event {

    }

    private static class TextChangedEvent implements Event {

        private final String text;


        private TextChangedEvent(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

}
