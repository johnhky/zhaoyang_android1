package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.PageActivity;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.adapter.DruglistAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lucas on 1/22/16.
 */
public class DrugActivity extends PageActivity2 {
    private DrugModule api = Api.of(DrugModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, DrugActivity.class);
        return i;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new DruglistAdapter(DrugActivity.this);
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
        api.orderList(getCallback().getPage()).enqueue(getCallback());
    }
}