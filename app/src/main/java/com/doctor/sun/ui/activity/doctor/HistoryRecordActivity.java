package com.doctor.sun.ui.activity.doctor;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityHistoryRecordBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.RecordHistoriesAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.DividerItemDecoration;

import retrofit.Call;

/**
 * 医生端 历史记录
 * Created by Lynn on 1/8/16.
 */
public class HistoryRecordActivity extends BaseActivity2 {
    private ActivityHistoryRecordBinding binding;
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private RecordHistoriesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history_record);

        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("历史记录");
        binding.setHeader(header);

        initView();
        initData();
    }

    private void initView() {
        binding.rvRecord.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvRecord.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.shape_divider), true));
    }

    private void initData() {
        mAdapter = new RecordHistoriesAdapter(this);
        binding.rvRecord.setAdapter(mAdapter);
        getRecordHistories();
    }

    private void getRecordHistories() {
        Call<ApiDTO<PageDTO<AppointMent>>> call = api.Patient(getIntent().getIntExtra(Constants.PARAM_RECORD_ID, 0) + "");
        call.enqueue(new PageCallback<AppointMent>(mAdapter) {
            @Override
            protected void handleResponse(PageDTO<AppointMent> response) {
                super.handleResponse(response);
                getAdapter().onFinishLoadMore(true);
            }
        });

    }
}
