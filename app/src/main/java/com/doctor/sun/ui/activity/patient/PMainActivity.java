package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMain2Binding;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Banner;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.handler.patient.PMainActivityHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.widget.AddMedicalRecordDialog;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateUtil;
import com.doctor.sun.vm.AutoScrollViewModel;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 14/7/2016.
 */

public class PMainActivity extends BaseFragmentActivity2 {


    private PActivityMain2Binding binding;
    private PMainActivityHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_main_2);
        FooterViewModel footer = getFooter();
        binding.setFooter(footer);
        handler = new PMainActivityHandler();
        binding.setHandler(handler);
        Patient patientProfile = Settings.getPatientProfile();
        if (patientProfile == null || "".equals(patientProfile.getName())) {
            if (patientProfile != null) {
                String phone = patientProfile.getPhone();
                if (Config.getBoolean(Constants.SHOULD_SHOW_ADD_RECORD_DIALOG + phone, true)) {
                    new AddMedicalRecordDialog(this, true).show(false);
                    Config.putBoolean(Constants.SHOULD_SHOW_ADD_RECORD_DIALOG + phone, false);
                }
            } else {
                new AddMedicalRecordDialog(this, true).show(false);
            }
        } else {
            showShowCase();
        }
        updateProfileInfo();
        loadBannerInfo();

        startActivity(new Intent(this, PMainActivity2.class));
    }

    private void updateProfileInfo() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Settings.setPatientProfile(response);
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
        return FooterViewModel.getInstance(R.id.tab_one);
    }

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(PMainActivity.class);
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

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals(TAG)) {
                finish();
            }
        }
    }

    public void showShowCase() {
//        if (ShowCaseUtil.shown(TAG)) {
//            return;
//        }
        if (!Settings.isDoctor()) {
            View view1 = binding.imageView;
            View view3 = binding.flyAfterService;
            View view2 = binding.flyOrders;
            View view4 = binding.includeFooter.flTabTwo;


            ShowCaseUtil.showCase2(view1, "点击这里可以找到适合您的医生", 1, 4, 0, false);
            ShowCaseUtil.showCase2(view2, "点这里可以查询所有类型的订单", -1, 4, 1, false);
            ShowCaseUtil.showCase2(view3, "点击这里向随访医生反馈诊后/出院后病情恢复情况", -1, 4, 2, false);
            ShowCaseUtil.showCase2(view4, "您可以在这里与医生通过文字信息或者电话进行沟通",-1  , 4, 3, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UpdateUtil.onPause();
        EventHub.unregister(binding.vpBanner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventHub.register(binding.vpBanner);
    }

    @Override
    public int getMidTitle() {
        return R.string.default_title;
    }

    public static Intent intentFor(Context context) {
        return new Intent(context, PMainActivity.class);
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {
        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
    }
}
