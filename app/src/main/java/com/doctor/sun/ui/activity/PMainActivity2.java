package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PMainActivity2Binding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.handler.patient.PMainHandler;

public class PMainActivity2 extends AppCompatActivity {

    private PMainActivity2Binding binding;

    private PMainHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_main_activity2);

        handler = new PMainHandler();
        binding.setData(handler);

        setPatientProfile();
        initRvMessage();
        initDoctorView();
    }

    private void initRvMessage() {
        SimpleAdapter adapter = handler.getMessageAdapter();
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessage.setAdapter(adapter);
    }

    private void initDoctorView() {
        SimpleAdapter adapter = handler.getDoctorAdapter();
        binding.rvRecommendDoctor.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecommendDoctor.setAdapter(adapter);
    }

    private void setPatientProfile() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                binding.setPatient(response);
            }
        });
    }
}
