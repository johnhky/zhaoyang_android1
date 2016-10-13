package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMainBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DoctorIndex;
import com.doctor.sun.entity.Version;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.handler.MainActivityHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.widget.BindingDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateEventHandler;
import com.doctor.sun.util.UpdateUtil;
import com.doctor.sun.vo.ClickMenu;
import com.squareup.otto.Subscribe;

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
    private boolean isShowing = false;
    private UpdateEventHandler updateEventHandler;

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
        boolean rejected = Doctor.STATUS_REJECT.equals(doctor.getStatus());
        boolean notChanged = Settings.lastDoctorStatus().equals(doctor.getStatus());
        if (notChanged && !rejected) {
            return;
        }
        switch (doctor.getStatus()) {
            case Doctor.STATUS_REJECT: {
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
            case Doctor.STATUS_PENDING: {
                ClickMenu menu = new ClickMenu(R.layout.dialog_pass, 0, "审核中", null);
                menu.setSubTitle("您的信息正在审核中，请耐心等待...");
                BindingDialog.newBuilder(this, menu)
                        .show();
                break;
            }
            case Doctor.STATUS_PASS: {
                ClickMenu menu = new ClickMenu(R.layout.dialog_pass, 0, "审核通过", null);
                menu.setSubTitle("欢迎加入昭阳医生");
                BindingDialog.newBuilder(this, menu).show();
                break;
            }
        }
        Settings.setLastDoctorStatus(doctor.getStatus());
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
        if (shouldCheck()) {
            UpdateUtil.checkUpdate(this);
        }
        showShowCase();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateEventHandler = UpdateEventHandler.register();

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
        UpdateEventHandler.unregister(updateEventHandler);
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

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals(TAG)) {
                finish();
            }
        }
    }

    public void showShowCase() {
        if (isShowing) {
            return;
        }
        if (ShowCaseUtil.isShow(TAG)) {
            return;
        }
        if (Settings.isDoctor()) {
            isShowing = true;
            ShowCaseUtil.showCase(binding.llyAppointment, "所有已预约患者都在这里", TAG, 3, 0, true);
            ShowCaseUtil.showCase(binding.llyAfterService, "点击这里向已出院患者或者就诊后的患者进行随访", TAG, 3, 1, true);
            ShowCaseUtil.showCase(binding.includeFooter.flTabTwo, "您可以在这里与患者通过文字信息或者电话进行沟通", TAG, 3, 2, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UpdateUtil.onPause();
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {
        Version data = e.getData();
        String versionName = e.getVersionName();

        boolean forceUpdate;
        double newVersion;
        if (data == null) {
            forceUpdate = false;
            newVersion = 0;
        } else {
            forceUpdate = data.getForceUpdate();
            newVersion = data.getNowVersion();
        }

        UpdateUtil.DownloadNewVersionCallback callback = new UpdateUtil.DownloadNewVersionCallback(data);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.canceledOnTouchOutside(false);
        builder.cancelable(false);
        builder.onPositive(callback);
        builder.positiveText("马上更新");
        if (forceUpdate) {
            builder.content("昭阳医生已经发布了最新版本，更新后才可以使用哦！").show();
        } else if (newVersion > Double.valueOf(versionName)) {
            builder.negativeText("稍后提醒我");
            builder.content("昭阳医生已经发布了最新版本，更新后会有更好的体验哦！").show();
        } else {
            if (UpdateUtil.noNewVersion != null) {
                UpdateUtil.noNewVersion.onNoNewVersion();
                UpdateUtil.noNewVersion = null;
            }
        }

        UpdateUtil.lastCheckTime = System.currentTimeMillis();
    }
}