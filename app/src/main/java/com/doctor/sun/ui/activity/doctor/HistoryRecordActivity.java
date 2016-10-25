package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityHistoryRecordBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.DividerItemDecoration;

import retrofit2.Call;

/**
 * 医生端 历史记录
 * Created by Lynn on 1/8/16.
 */
public class HistoryRecordActivity extends BaseFragmentActivity2 {
    private ActivityHistoryRecordBinding binding;
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private SimpleAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_record);

        initView();
        initData();
    }

    private void initView() {
        binding.rvRecord.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvRecord.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.shape_divider), true));
    }

    private void initData() {
        mAdapter = new SimpleAdapter<>(this);
        mAdapter.mapLayout(R.layout.item_appointment, R.layout.p_item_history);
        binding.rvRecord.setAdapter(mAdapter);
        getRecordHistories();
    }

    private void getRecordHistories() {
        Call<ApiDTO<PageDTO<Appointment>>> call = api.Patient(getIntent().getIntExtra(Constants.PARAM_RECORD_ID, 0) + "");
        call.enqueue(new PageCallback<Appointment>(mAdapter) {
            @Override
            protected void handleResponse(PageDTO<Appointment> response) {
                super.handleResponse(response);
                getAdapter().onFinishLoadMore(true);
            }
        });
    }

    public static Intent intentFor(Context context, int recordId) {
        return new Intent(context, HistoryRecordActivity.class)
                .putExtra(Constants.PARAM_RECORD_ID, recordId);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_history;
    }
}
