package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.HashMap;

/**
 * Created by rick on 1/3/2016.
 */
@Factory(type = BaseFragment.class, id = "DrugListFragment")
public class DrugListFragment extends RefreshListFragment {
    public static final String TAG = DrugListFragment.class.getSimpleName();

    public static final String COUPON_ID = "couponId";
    private DrugModule api = Api.of(DrugModule.class);
    private MultiSelectAdapter adapter;
    private static HashMap<String, String> drugExtraField = new HashMap<>();

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        return bundle;
    }

    public static DrugListFragment getInstance() {
        return new DrugListFragment();
    }

    public static HashMap<String, String> getDrugExtraField() {
        drugExtraField.put("body", "drug order");
        return drugExtraField;
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new MultiSelectAdapter();
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.orderList(getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "没有任何寄药订单";
    }
}
