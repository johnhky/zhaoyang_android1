package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;


/**
 * 历史纪录列表
 * <p/>
 * Created by lucas on 1/4/16.
 */
public class HistoryActivity extends PageActivity2 {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);

    public static Intent makeIntent(Context context, int recordId) {
        Intent i = new Intent(context, HistoryActivity.class);
        i.putExtra(Constants.DATA, recordId);
        return i;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_history);
        return adapter;
    }


    @Override
    protected void loadMore() {
        super.loadMore();

        int recordId = getIntent().getIntExtra(Constants.DATA, 0);
        api.recordOrders(0, recordId, "").enqueue(getCallback());
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有历史记录";
    }

    @Override
    public int getMidTitle() {
        return R.string.title_history;
    }
}
