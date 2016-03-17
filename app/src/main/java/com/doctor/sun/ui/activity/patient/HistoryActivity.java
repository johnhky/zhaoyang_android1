package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.PageActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * 历史纪录列表
 * <p/>
 * Created by lucas on 1/4/16.
 */
public class HistoryActivity extends PageActivity {
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, HistoryActivity.class);
        return i;
    }

    @NonNull
    @Override
    protected SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_history);
        return adapter;
    }

    @NonNull
    @Override
    protected HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("历史记录");
    }

    @Override
    protected void loadMore() {
        api.histories(Integer.parseInt(getCallback().getPage())).enqueue(getCallback());
    }
}
