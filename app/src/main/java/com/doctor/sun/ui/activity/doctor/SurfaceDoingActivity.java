package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityOrderSurfaceDoingBinding;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.activity.patient.EditQuestionActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.util.JacksonUtils;

/**
 * Created by heky on 17/5/27.
 */

public class SurfaceDoingActivity extends BaseFragmentActivity2 {

    ActivityOrderSurfaceDoingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_surface_doing);
        if (null != getData()) {
            Appointment appointment = getData();
            binding.tvAddress.setText(appointment.getDoctor().getClinicAddress().getAddress());
            binding.tvBack.setText(appointment.getRecord().getPatientName());
            binding.tvTime.setText(appointment.getVisit_time());

        }
        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnAskServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showService();
            }
        });
        if (Settings.isDoctor()) {
            binding.tvQuestion.setText("患者问卷");
            binding.tvRecord.setText("病历记录");
        } else {
            binding.tvQuestion.setText("我的问卷");
            binding.tvRecord.setText("医生建议");
        }
        binding.tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isDoctor()) {
                    AppointmentHandler2.answerQuestion(SurfaceDoingActivity.this, 0, getData());
                } else {
                    Intent intent = EditQuestionActivity.intentFor(SurfaceDoingActivity.this, getData().getId(), QuestionsPath.NORMAL);
                    startActivity(intent);
                }
            }
        });
        binding.tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isDoctor()) {
                    AppointmentHandler2.answerQuestion(SurfaceDoingActivity.this, 1, getData());
                } else {
                    Intent intent = AfterServiceDoingActivity.intentFor(SurfaceDoingActivity.this, getData().getId(), getData().getRecord_id(), 1);
                    startActivity(intent);
                }

            }
        });
    }

    public void showService() {
        Intent intent = MedicineStoreActivity.intentForCustomerService(SurfaceDoingActivity.this);
        startActivity(intent);
    }

    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, SurfaceDoingActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return i;
    }


    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }


}
