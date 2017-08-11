package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMainBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DoctorIndex;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.ProgressEvent;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.handler.MainActivityHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.widget.BindingDialog;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateUtil;
import com.doctor.sun.vm.ClickMenu;
import com.squareup.otto.Subscribe;

import java.io.File;

import io.ganguo.library.Config;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Created by rick on 10/23/15.
 */
public class MainActivity extends BaseDoctorActivity {

    public static final int IS_FIRST_TIME = 1;
    public static final int NOT_FIRST_TIME = 2;
    public static final String DOCTOR_INDEX = "DOCTOR_INDEX";
    private ProfileModule api = Api.of(ProfileModule.class);
    private ActivityMainBinding binding;
    private RealmResults<TextMsg> unReadMsg;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setFooter(getFooter());
        binding.setHandler(new MainActivityHandler());

        showStatusDialog();
        String string = Config.getString(DOCTOR_INDEX);
        if (string != null && !string.equals("")) {
            setDoctorIndex(JacksonUtils.fromJson(string, DoctorIndex.class));
        }
        unReadMsg = getRealm().where(TextMsg.class).equalTo("haveRead", false).findAll();
        unReadMsg.addChangeListener(new RealmChangeListener<RealmResults<TextMsg>>() {
            @Override
            public void onChange(RealmResults<TextMsg> element) {
                binding.setCount(element.size());
            }
        });
        binding.setCount(unReadMsg.size());
    }

    /**
     * 弹出医生修改资料审核状态的弹窗
     */
    public void showStatusDialog() {
//        boolean isFirstTime = Config.getInt(Constants.PASSFIRSTTIME, -1) == IS_FIRST_TIME;
//        if (isFirstTime) {
        final Doctor doctor = Settings.getDoctorProfile();
        if (doctor == null) {
            return;
        }
        boolean rejected = ReviewStatus.STATUS_REJECTED.equals(doctor.getReviewStatus());
        boolean notChanged = Settings.lastDoctorStatus().equals(doctor.getReviewStatus());
        if (notChanged && !rejected) {
            return;
        }
        switch (doctor.getReviewStatus()) {
            case ReviewStatus.STATUS_REJECTED: {
                ClickMenu menu = new ClickMenu(R.layout.dialog_pass, 0, "审核未能通过", null);
                menu.setSubTitle("您可以修改信息并再次发起审核请求");
                BindingDialog.newBuilder(this, menu)
                        .negativeColor(Color.GRAY)
                        .negativeText("暂时不用")
                        .positiveText("马上修改")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent i = EditDoctorInfoFragment.intentFor(MainActivity.this, doctor);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            }
            case ReviewStatus.STATUS_PENDING: {
                ClickMenu menu = new ClickMenu(R.layout.dialog_pass, 0, "审核中", null);
                menu.setSubTitle("您的信息正在审核中，请耐心等待...");
                BindingDialog.newBuilder(this, menu)
                        .show();
                break;
            }
            case ReviewStatus.STATUS_PASS: {
                ClickMenu menu = new ClickMenu(R.layout.dialog_pass, 0, "审核通过", null);
                menu.setSubTitle("欢迎加入昭阳医生");
                BindingDialog.newBuilder(this, menu).show();
                break;
            }
        }
        Settings.setLastDoctorStatus(doctor.getReviewStatus());
    }

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(R.id.tab_one);
    }

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(MainActivity.class);
                break;
            }
            case 1: {
                startActivity(ConsultingActivity.class);
                break;
            }
            case 2: {
                startActivity(MeActivity.class);
                break;
            }
        }
    }


    private void setDoctorIndex(DoctorIndex doctorIndex) {
        if (doctorIndex != null) {
            binding.setData(doctorIndex);
            binding.executePendingBindings();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        UpdateUtil.checkUpdate(this,1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRealm().addChangeListener(getFooter());
        api.doctorIndex().enqueue(new ApiCallback<DoctorIndex>() {
            @Override
            protected void handleResponse(DoctorIndex response) {
                setDoctorIndex(response);
                Config.putString(DOCTOR_INDEX, JacksonUtils.toJson(response));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unReadMsg != null) {
            unReadMsg.removeChangeListeners();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UpdateUtil.STORE_REQUEST) {
            boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
            if (isGranted) {
                if (shouldCheck()) {
                    UpdateUtil.checkUpdate(this,1);
                }
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {
        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
    }
}