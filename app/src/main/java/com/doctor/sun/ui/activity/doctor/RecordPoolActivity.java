package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/6/2016.
 */
public class RecordPoolActivity extends PageActivity2 {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, RecordPoolActivity.class);
        return intent;
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
        api.recordPool(getCallback().getPage(), keyword).enqueue(getCallback());
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何记录";
    }

    @Override
    protected void onPrepareHeader() {
        super.onPrepareHeader();
        insertSearchItem();
    }

    @Override
    public int getMidTitle() {
        return R.string.title_record_pool;
    }
}
