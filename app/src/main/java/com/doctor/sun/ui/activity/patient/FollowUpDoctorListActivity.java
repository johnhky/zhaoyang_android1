package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * 历史纪录列表
 * <p/>
 * Created by lucas on 1/4/16.
 */
public class FollowUpDoctorListActivity extends PageActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, FollowUpDoctorListActivity.class);
        return i;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_follow_up_doctor);
        return adapter;
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        api.applyingDoctorList("", "follow", getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有随访医生";
    }

    @Override
    public int getMidTitle() {
        return R.string.title_follow_up_doctor;
    }
}
