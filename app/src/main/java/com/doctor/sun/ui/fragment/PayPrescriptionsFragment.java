package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.PayPrescriptionsModel;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.PayEventHandler;

import java.util.List;

/**
 * Created by rick on 14/9/2016.
 * 寄药订单支付界面
 */

public class PayPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = PayPrescriptionsFragment.class.getSimpleName();

    private PayPrescriptionsModel model;
    private PayEventHandler payEventHandler;

    public static Bundle getArgs(String drugId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, drugId);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new PayPrescriptionsModel();
        payEventHandler = PayEventHandler.register();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        DrugModule api = Api.of(DrugModule.class);
        api.drugDetail(getDrugId()).enqueue(new SimpleCallback<Drug>() {
            @Override
            protected void handleResponse(Drug response) {
                List<SortedItem> sortedItems =
                        model.parseData(response);
                binding.swipeRefresh.setRefreshing(false);
                getAdapter().insertAll(sortedItems);
            }
        });
    }

    private String getDrugId() {
        return getArguments().getString(Constants.DATA);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PayEventHandler.unregister(payEventHandler);
    }
}
