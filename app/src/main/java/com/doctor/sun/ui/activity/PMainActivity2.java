package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PMainActivity2Binding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.activity.patient.PMeActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.handler.patient.PMainHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

public class PMainActivity2 extends AppCompatActivity {

    private PMainActivity2Binding binding;

    private PMainHandler handler;

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, PMainActivity2.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_main_activity2);

        handler = new PMainHandler();
        binding.setData(handler);

        FooterViewModel footer = getFooter();
        binding.setFooter(footer);

        setPatientProfile();
        initRvMessage();
        initDoctorView();
    }

    private void initRvMessage() {
        SimpleAdapter adapter = handler.getMessageAdapter();
        adapter.onFinishLoadMore(true);
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessage.setAdapter(adapter);
    }

    private void initDoctorView() {
        SimpleAdapter adapter = handler.getDoctorAdapter();
        adapter.onFinishLoadMore(true);
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

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(R.id.tab_one);
    }

    private void startActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
        overridePendingTransition(0, 0);
    }

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(PMainActivity2.class);
                break;
            }
            case 1: {
                startActivity(PConsultingActivity.class);
                break;
            }
            case 2: {
                startActivity(PMeActivity.class);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UpdateUtil.STORE_REQUEST) {
            boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
            if (isGranted) {
                UpdateUtil.checkUpdate(this);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventHub.register(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        UpdateUtil.checkUpdate(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventHub.unregister(this);
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {
        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
    }
}
