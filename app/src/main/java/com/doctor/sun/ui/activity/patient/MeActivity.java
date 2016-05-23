package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMeBinding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.patient.handler.MeHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/4/16.
 */
public class MeActivity extends BaseActivity2 {
    private PActivityMeBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);
    private MeHandler handler;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_me);
        FooterViewModel footer = getFooter();
        binding.setFooter(footer);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("我");
        Patient patient = getData();
        binding.setData(patient);
        handler = new MeHandler(patient);
        binding.setHandler(handler);
        binding.setHeader(header);

        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Patient data = response.getInfo();
                Config.putString(Constants.PATIENT_PROFILE, JacksonUtils.toJson(response));
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
        String json = Config.getString(Constants.PATIENT_PROFILE);
        PatientDTO patient = JacksonUtils.fromJson(json, PatientDTO.class);
        return patient.getInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
}
