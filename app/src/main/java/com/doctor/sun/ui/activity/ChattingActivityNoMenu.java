package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.util.JacksonUtils;

/**
 * Created by rick on 30/5/2016.
 */
public class ChattingActivityNoMenu extends ChattingActivity {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, ChattingActivityNoMenu.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        return i;
    }
}
