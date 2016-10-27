package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.PageActivity2;


/**
 * Created by rick on 11/20/15.
 */
public class AppointmentListActivity extends PageActivity2 {
    private AppointmentModule api = Api.of(AppointmentModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AppointmentListActivity.class);
        return i;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.searchAppointment(getCallback().getPage(), keyword, "").enqueue(getCallback());
    }

    @Override
    protected void onPrepareHeader() {
        super.onPrepareHeader();
        insertSearchItem();
    }


    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有预约任何患者";
    }

    @Override
    public int getMidTitle() {
        return R.string.title_appointment_list;
    }

}
