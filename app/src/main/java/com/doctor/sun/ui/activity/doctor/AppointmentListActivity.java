package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * Created by rick on 11/20/15.
 */
public class AppointmentListActivity extends PageActivity2 {
    private AppointmentModule api = Api.of(AppointmentModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AppointmentListActivity.class);
        return i;
    }

//    @Override
//    public void onMenuClicked() {
//        Intent intent = UrgentListActivity.intentFor(this);
//        startActivity(intent);
//    }

    @Override
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(createHeaderViewModel());
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.doctorAppointment(getCallback().getPage(), "1").enqueue(getCallback());
    }

    @NonNull
    protected HeaderViewModel createHeaderViewModel() {
        return null;
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
