package com.doctor.sun.ui.activity.patient;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMain2Binding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Banner;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.handler.patient.PMainActivityHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.ui.widget.AddMedicalRecordDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.UpdateUtil;
import com.doctor.sun.vo.AutoScrollViewModel;

import java.util.List;

import io.ganguo.library.Config;

/**
 * Created by rick on 14/7/2016.
 */

public class PMainActivity2 extends BaseFragmentActivity2 {


    private PActivityMain2Binding binding;
    private PMainActivityHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_main_2);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("昭阳医生");
        binding.setHeader(header);
        FooterViewModel footer = getFooter();
        binding.setFooter(footer);
        handler = new PMainActivityHandler();
        binding.setHandler(handler);
        Patient patientProfile = TokenCallback.getPatientProfile();
        if (patientProfile == null || "".equals(patientProfile.getName())) {
            if (patientProfile != null) {
                String phone = patientProfile.getPhone();
                if (Config.getBoolean(Constants.SHOULD_SHOW_ADD_RECORD_DIALOG + phone, true)) {
                    new AddMedicalRecordDialog(this, true).show();
                    Config.putBoolean(Constants.SHOULD_SHOW_ADD_RECORD_DIALOG + phone, false);
                }
            }else {
                new AddMedicalRecordDialog(this, true).show();
            }
        }
        updateProfileInfo();
        loadBannerInfo();
    }

    private void updateProfileInfo() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Config.putString(Constants.PATIENT_PROFILE, JacksonUtils.toJson(response));
                binding.setData(response);
            }
        });
    }

    private void loadBannerInfo() {
        ToolModule api = Api.of(ToolModule.class);
        api.patientBanner().enqueue(new SimpleCallback<List<Banner>>() {
            @Override
            protected void handleResponse(List<Banner> response) {
                if (response == null || response.isEmpty()) {
                    return;
                }
                AutoScrollViewModel autoScrollViewModel = new AutoScrollViewModel();
                autoScrollViewModel.setBannerImages(response);
                binding.setBanner(autoScrollViewModel);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (shouldCheck()) {
            UpdateUtil.checkUpdate(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UpdateUtil.STORE_REQUEST) {
            boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
            if (isGranted) {
                if (shouldCheck()) {
                    UpdateUtil.checkUpdate(this);
                }
            }
        }
    }

    private FooterViewModel getFooter() {
        PatientFooterView mView = new PatientFooterView(this);
        return FooterViewModel.getInstance(mView, R.id.tab_one);
    }
}
