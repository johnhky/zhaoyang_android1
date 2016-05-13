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
 * Created by rick on 11/25/15.
 */
public class UrgentListActivity extends PageActivity2 {

    private AppointmentModule api = Api.of(AppointmentModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, UrgentListActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdapter().mapLayout(R.layout.item_appointment, R.layout.item_urgent_call);
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(getHeaderViewModel());
    }

    @Override
    protected void loadMore() {
        api.urgentCalls(getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("紧急咨询")
                .setRightTitle("已预约患者");
        return header;
    }

    @Override
    public void onMenuClicked() {
        Intent intent = AppointmentListActivity.makeIntent(this);
        startActivity(intent);
    }

}
