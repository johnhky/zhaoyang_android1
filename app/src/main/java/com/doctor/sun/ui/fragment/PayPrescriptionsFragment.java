package com.doctor.sun.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.DrugOrderDetail;
import com.doctor.sun.model.PayPrescriptionsModel;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.PayEventHandler;

import java.util.List;

/**
 * Created by rick on 14/9/2016.
 * 寄药订单支付界面
 */
@Factory(type = BaseFragment.class, id = "PayPrescriptionsFragment")
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
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateSuccess");
        getActivity().registerReceiver(receiver, filter);
        model = new PayPrescriptionsModel();
        payEventHandler = PayEventHandler.register(getActivity());
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
        api.drugDetail(getDrugId()).enqueue(new SimpleCallback<DrugOrderDetail>() {

            @Override
            protected void handleResponse(DrugOrderDetail response) {
                List<SortedItem> sortedItems = model.parseData(getContext(), response);
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
        getActivity().unregisterReceiver(receiver);
        PayEventHandler.unregister(payEventHandler);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals("updateSuccess")) {
                DrugModule api = Api.of(DrugModule.class);
                api.drugDetail(getDrugId()).enqueue(new SimpleCallback<DrugOrderDetail>() {

                    @Override
                    protected void handleResponse(DrugOrderDetail response) {
                        List<SortedItem> sortedItems = model.parseData(getContext(), response);
                        binding.swipeRefresh.setRefreshing(false);
                        getAdapter().insertAll(sortedItems);
                    }

                });
            }
        }
    };


}
