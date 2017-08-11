package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by heky on 17/5/27.
 */

public class MyPatientActivity extends PageActivity2{

    ProfileModule api = Api.of(ProfileModule.class);
    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, MyPatientActivity.class);
        return intent;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.item_my_patient);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.getPatientList().enqueue(getCallback());
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "暂时没有任何患者";
    }

    @Override
    public int getMidTitle() {
        return R.string.my_patient;
    }
}
