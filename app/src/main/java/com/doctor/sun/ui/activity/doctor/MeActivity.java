package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMeBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.handler.MeHandler;
import com.doctor.sun.ui.model.FooterViewModel;

import com.doctor.sun.util.JacksonUtils;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;


/**
 * Created by rick on 10/23/15.
 * 22我的收藏
 */
public class MeActivity extends BaseDoctorActivity {

    private ActivityMeBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_me);
        binding.setFooter(getFooter());

        Doctor doctor = Settings.getDoctorProfile();
        binding.setData(doctor);
        binding.setHandler(new MeHandler(doctor));
        if (doctor != null) {
            String level = doctor.getLevel();
            if (level == null || "".equals(level)) {
                binding.tvTest.setVisibility(View.GONE);
            } else {
                binding.tvTest.setText(level);
            }
        } else {
            binding.tvTest.setVisibility(View.GONE);
        }

        api.doctorProfile().enqueue(new ApiCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                Doctor data = response;
                Config.putString(Constants.DOCTOR_PROFILE, JacksonUtils.toJson(data));
                binding.setData(data);
            }
        });
    }

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(this, R.id.tab_three);
    }


    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals("doctorMe")) {
                finish();
            }
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_me;
    }
}
