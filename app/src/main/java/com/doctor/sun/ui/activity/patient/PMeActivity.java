package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMeBinding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.handler.MeHandler;
import com.doctor.sun.ui.model.FooterViewModel;

import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.util.JacksonUtils;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/4/16.
 */
public class PMeActivity extends BaseFragmentActivity2 {
    private PActivityMeBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);
    private MeHandler handler;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PMeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_me);
        FooterViewModel footer = getFooter();
        binding.setFooter(footer);
        Patient patient = getData();
        binding.setData(patient);
        handler = new MeHandler(patient);
        binding.setHandler(handler);

        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Patient data = response.getInfo();
                Settings.setPatientProfile(response);
                handler.setData(data);
                binding.setData(data);
            }
        });
    }

    private FooterViewModel getFooter() {
        PatientFooterView mView = new PatientFooterView(this);

        return FooterViewModel.getInstance(mView, R.id.tab_three);
    }

    public Patient getData() {
        return Settings.getPatientProfile();
    }

    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals("patientMe")) {
                finish();
            }
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_me;
    }
}
