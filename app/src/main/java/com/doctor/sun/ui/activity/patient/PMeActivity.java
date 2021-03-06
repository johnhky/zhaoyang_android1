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
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.PatientProfileChangedEvent;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.patient.handler.MeHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;

/**
 * Created by lucas on 1/4/16.
 */
public class PMeActivity extends BaseFragmentActivity2 {
    private PActivityMeBinding binding;

    private MeHandler handler;
    private Runnable runnable;

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
        handler = new MeHandler(patient);
        binding.setHandler(handler);

    }

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(R.id.tab_three);
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

    public Patient getData() {
        return Settings.getPatientProfile();
    }

    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }

    public static void refreshProfile() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new PatientDTOApiCallback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfile();
    }


    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if ("patientMe".equals(e.id)) {
                finish();
            }
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_me;
    }

    @Subscribe
    public void onEventMainThread(PatientProfileChangedEvent e) {
        handler.setData(e.getResponse().getInfo());
        String review_status = e.getResponse().getInfo().getReview_status();
        if (ReviewStatus.STATUS_PENDING.equals(review_status)) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (Settings.isLogin()) {
                        refreshProfile();
                    }
                }
            };
            Tasks.runOnUiThread(runnable, 5000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Tasks.removeRunnable(runnable);
    }

    private static class PatientDTOApiCallback extends ApiCallback<PatientDTO> {


        PatientDTOApiCallback() {
        }

        @Override
        protected void handleResponse(PatientDTO response) {
            Settings.setPatientProfile(response);
            EventHub.post(new PatientProfileChangedEvent(response));
        }
    }

}
