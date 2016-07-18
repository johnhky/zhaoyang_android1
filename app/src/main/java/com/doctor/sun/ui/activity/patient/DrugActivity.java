package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.http.Api;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 1/22/16.
 */
public class DrugActivity extends PageActivity2 {
    private DrugModule api = Api.of(DrugModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, DrugActivity.class);
        return i;
    }

    @Override
    protected void initHeader() {
        HeaderViewModel headerViewModel = getHeaderViewModel();
        getBinding().setHeader(headerViewModel);
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("寄药订单");
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.orderList(getCallback().getPage()).enqueue(getCallback());
    }
}