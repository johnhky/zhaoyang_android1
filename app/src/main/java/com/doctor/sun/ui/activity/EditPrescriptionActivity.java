package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityEditPrescriptionBinding;
import com.doctor.sun.immutables.Prescription;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by heky on 17/6/28.
 */

public class EditPrescriptionActivity extends BaseFragmentActivity2 implements View.OnClickListener {

    public ActivityEditPrescriptionBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(EditPrescriptionActivity.this, R.layout.activity_edit_prescription);
        initView();
        initLisitener();
    }

    public void initView() {
        if (null != getPrescription()) {
            binding.tvMiddleTitle.setText("编辑/添加用药");
        } else {
            Prescription data = getPrescription();
            binding.tvMiddleTitle.setText("处方详情");
            binding.llMain.setEnabled(false);
            binding.etDrugName.setText(data.getDrug_name());
            binding.etTakeDays.setText(data.getTake_medicine_days());
            binding.tvFrequency.setText(data.getFrequency());
            binding.tvDrugUnit.setText(data.getDrug_unit());
            if (data.getTitration().size() > 0) {
                binding.llTitations.setEnabled(false);
                binding.chxOpen.setChecked(false);
                binding.llFrequency.setVisibility(View.GONE);
                binding.llNoTitration.setVisibility(View.GONE);
                binding.llDrugUnit.setVisibility(View.GONE);
            }
        }
    }

    public void initLisitener() {
        binding.tvBack.setOnClickListener(this);
        binding.tvDone.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_done:

                break;

        }
    }

    public Prescription getPrescription() {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(getIntent().getStringExtra(Constants.DATA), Prescription.class);

    }

}
