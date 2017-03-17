package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by test on 17/3/3.
 * 某个病人看病的历史记录activity
 */

public class SinglePatientHistoryActivity extends PageActivity2{
    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    public String id="";

//从之前跳转的界面获取需要的数据
    @Override
    public void getDataFrom() {
        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("id");
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.item_record_pool);

        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.singleHistory(id).enqueue(getCallback());

    }
    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何记录";
    }



}
