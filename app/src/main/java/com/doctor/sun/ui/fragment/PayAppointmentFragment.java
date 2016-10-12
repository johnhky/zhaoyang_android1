package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.adapter.MapLayoutIdInterceptor;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.PayEventHandler;
import com.doctor.sun.vo.BaseItem;

import java.util.List;

/**
 * Created by rick on 14/9/2016.
 * 寄药订单支付界面
 */

public class PayAppointmentFragment extends SortedListFragment {
    public static final String TAG = PayAppointmentFragment.class.getSimpleName();

    private final AppointmentModule api = Api.of(AppointmentModule.class);
    public AppointmentBuilder builder;
    private PayEventHandler payEventHandler;

    public static Bundle getArgs(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AppointmentBuilder();
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
        api.appointmentDetail(getAppointmentId()).enqueue(new SimpleCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
                builder.setDoctor(response.getDoctor());
                builder.setRecord(response.getUrgentRecord());
                List<BaseItem> sortedItems = builder.toSortedItems(response);
                for (int i = 0; i < sortedItems.size(); i++) {
                    BaseItem item = sortedItems.get(i);
                    if (item != null) {
                        item.setPosition(i);
                        item.setItemId("i" + i);
                        getAdapter().insert(item);
                    }
                }
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
        MapLayoutIdInterceptor idInterceptor = new MapLayoutIdInterceptor();
        idInterceptor.put(R.layout.item_doctor, R.layout.item_base_doctor_info);
        idInterceptor.put(R.layout.item_r_medical_record, R.layout.p_item_record2);
        adapter.setLayoutIdInterceptor(idInterceptor);
        return adapter;
    }

    private String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        PayEventHandler.unregister(payEventHandler);
    }
}
