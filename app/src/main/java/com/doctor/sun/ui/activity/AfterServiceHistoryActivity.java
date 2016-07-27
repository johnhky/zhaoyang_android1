package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceHistoryActivity extends PageActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context, int recordId) {
        Intent intent = new Intent(context, AfterServiceHistoryActivity.class);
        intent.putExtra(Constants.DATA, recordId);
        return intent;
    }

    private int getData() {
        return getIntent().getIntExtra(Constants.DATA, -1);
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(getHeaderViewModel());
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        api.histories(getData(), getCallback().getPage()).enqueue(getCallback());
    }


    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("历史随访");
        return headerViewModel;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        if (!Settings.isDoctor()) {
            adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service2);
        }else {
            adapter.mapLayout(R.layout.item_after_service, R.layout.item_after_service2);
        }
        return adapter;
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何历史随访";
    }
}
