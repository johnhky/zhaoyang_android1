package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityOrderSurfaceFinishedBinding;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.DoctorDetailActivity2;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.util.JacksonUtils;

/**
 * Created by heky on 17/5/27.
 */

public class SurfaceFinishedActivity extends BaseFragmentActivity2 {

    ActivityOrderSurfaceFinishedBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_surface_finished);
        if(Settings.isDoctor()){
            binding.btnAppointment.setVisibility(View.GONE);
            binding.btnAsk.setText("咨询客服");
        }else{
            binding.tvQuestion.setText("我的问卷");
            binding.tvRecord.setText("医生建议");
            binding.tvRemind.setVisibility(View.VISIBLE);
        }
        binding.btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SurfaceFinishedActivity.this, DoctorDetailActivity2.class);
                intent.putExtra(Constants.DATA, getData().getDoctor());
                startActivity(intent);
            }
        });
        binding.btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MedicineStoreActivity.intentForCustomerService(SurfaceFinishedActivity.this);
                startActivity(intent);
            }
        });
        binding.tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isDoctor()){
                    AppointmentHandler2.answerQuestion(SurfaceFinishedActivity.this, 0, getData());
                }else{
                    Intent intent = AppointmentDetailActivity.makeIntent(SurfaceFinishedActivity.this,getData(),0);
                    startActivity(intent);
                }
            }
        });
        binding.tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isDoctor()){
                    AppointmentHandler2.answerQuestion(SurfaceFinishedActivity.this, 1, getData());
                }else{
                    Intent intent = AppointmentDetailActivity.makeIntent(SurfaceFinishedActivity.this,getData(),1);
                    startActivity(intent);
                }
            }
        });
    }


    public Appointment getData(){
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }

    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, SurfaceFinishedActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return i;
    }
}
