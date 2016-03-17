package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.PageActivity;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * Created by rick on 11/25/15.
 */
public class UrgentListActivity extends PageActivity {

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
    protected void loadMore() {
        api.urgentCalls(getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    @Override
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
