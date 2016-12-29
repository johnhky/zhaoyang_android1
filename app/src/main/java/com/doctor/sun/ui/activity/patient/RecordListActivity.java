package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityRecordListBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.handler.MedicalRecordHandler;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 患者管理
 * Created by lucas on 1/4/16.
 */
public class RecordListActivity extends BaseFragmentActivity2 {
    private Logger logger = LoggerFactory.getLogger(RecordListActivity.class);
    private PActivityRecordListBinding binding;
    private SimpleAdapter mAdapter;
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, RecordListActivity.class);
        return i;
    }

    public static Intent makeIntent(Context context, boolean showDialog) {
        Intent i = makeIntent(context);
        i.putExtra(Constants.DATA, showDialog);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        if (getIntent().getBooleanExtra(Constants.DATA, false)) {
//            new AddMedicalRecordDialog(RecordListActivity.this, false).show();
            showDialog();
        }
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
//                new AddMedicalRecordDialog(RecordListActivity.this, false).show();
                showDialog();
            }
        });
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_record_list);
        mAdapter = new SimpleAdapter();
        mAdapter.mapLayout(R.layout.item_r_medical_record, R.layout.p_item_recordlist);
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

                if (mAdapter.isEmpty()) {
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
            }
        });
    }

    public void showDialog() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.medicalRecordList().enqueue(new SimpleCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                MedicalRecordHandler.newRecord(RecordListActivity.this, response);
            }
        });
    }

    @Override
    public int getMidTitle() {
        return R.string.title_manage_patient;
    }
}
