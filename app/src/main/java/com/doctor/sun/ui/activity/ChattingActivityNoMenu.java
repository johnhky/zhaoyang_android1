package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;

/**
 * Created by rick on 30/5/2016.
 */
public class ChattingActivityNoMenu extends ChattingActivity {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, ChattingActivityNoMenu.class);
        i.putExtra(Constants.DATA, appointment);
        return i;
    }
}
