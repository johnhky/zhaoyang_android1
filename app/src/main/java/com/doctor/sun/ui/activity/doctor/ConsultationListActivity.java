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
public class ConsultationListActivity extends PageActivity {
    private AppointmentModule api = Api.of(AppointmentModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, ConsultationListActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAdapter().mapLayout(R.layout.item_appointment, R.layout.item_consultation);
    }

    @Override
    protected void loadMore() {
        api.consultations(getCallback().getPage()).enqueue(getCallback());

        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }

    @NonNull
    @Override
    protected HeaderViewModel getHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("转诊患者");
        return headerViewModel;
    }

}
