package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PActivityRecordListBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.RecordListAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.AddMedicalRecordDialog;

import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 病历管理
 * Created by lucas on 1/4/16.
 */
public class RecordListActivity extends BaseActivity2 {
    private Logger logger = LoggerFactory.getLogger(RecordListActivity.class);
    private PActivityRecordListBinding binding;
    private RecordListAdapter mAdapter;
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, RecordListActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initListener() {
        binding.tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddMedicalRecordDialog(RecordListActivity.this).show();
            }
        });
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_record_list);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("病历管理");
        binding.setHeader(header);
        mAdapter = new RecordListAdapter(this);
        mAdapter.mapLayout(R.layout.item_text, R.layout.p_item_recordlist);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvList.setAdapter(mAdapter);
    }

    private void initData() {
        api.medicalRecordList().enqueue(new ApiCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                mAdapter.clear();
                mAdapter.addAll(response);
                mAdapter.notifyDataSetChanged();
                mAdapter.onFinishLoadMore(true);
            }
        });
    }
}
