package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMedicalRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 1/12/16.
 */
public class MedicalRecordDetailActivity extends BaseActivity2 {
    private boolean isEditMode;
    private PActivityMedicalRecordBinding binding;
    private HeaderViewModel header = new HeaderViewModel(this);

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public static Intent makeIntent(Context context, MedicalRecord data) {
        Intent i = new Intent(context, MedicalRecordDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private MedicalRecord getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_medical_record);
        header.setMidTitle("病历详情");
        binding.setHeader(header);
        binding.setData(getData());
    }

    private void initData() {
        if (getData().getRelation().equals("本人")) {
            binding.myRecord.root.setVisibility(View.VISIBLE);
            binding.myRecord.etName.setText(getData().getName());
            binding.myRecord.etEmail.setText(getData().getEmail());
            binding.myRecord.etName.setFocusable(false);
            binding.myRecord.etEmail.setFocusable(false);
        } else {
            binding.othersRecord.root.setVisibility(View.VISIBLE);
            binding.othersRecord.etSelfName.setText(getData().getName());
            binding.othersRecord.etPatientName.setText(getData().getPatientName());
            binding.othersRecord.etEmail.setText(getData().getEmail());
            binding.othersRecord.etRelation.setText(getData().getRelation());
            binding.othersRecord.etPatientName.setFocusable(false);
            binding.othersRecord.etSelfName.setFocusable(false);
            binding.othersRecord.etRelation.setFocusable(false);
            binding.othersRecord.etEmail.setFocusable(false);
        }
        if (getData().getAddress().isEmpty())
            binding.tvAddress.setText("无");
        else
            binding.tvAddress.setText(getData().getAddress());

        if(getData().getIdentityNumber().isEmpty())
            binding.tvIdentityNumber.setText("无");
        else
            binding.tvIdentityNumber.setText(getData().getIdentityNumber());
    }
}
