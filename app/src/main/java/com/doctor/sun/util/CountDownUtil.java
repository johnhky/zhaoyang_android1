package com.doctor.sun.util;

import android.widget.TextView;

import java.util.Locale;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 22/8/2016.
 */

public class CountDownUtil {

    private static int ONE_SECOND = 1000;

    public static void countDown(final TextView tv, final String string, final String originText, final int remainTimeSeconds) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (remainTimeSeconds > 0) {
                    tv.setEnabled(false);
                    tv.setText(String.format(Locale.CHINA, string, remainTimeSeconds));
                    countDown(tv, string, originText, remainTimeSeconds - 1);
                } else {
                    tv.setEnabled(true);
                    tv.setText(originText);
                }
            }
        }, ONE_SECOND);

    }


}
