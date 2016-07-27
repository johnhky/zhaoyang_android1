package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMainBinding;
import com.doctor.sun.entity.DoctorIndex;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.handler.MainActivityHandler;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.widget.PassDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;


/**
 * Created by rick on 10/23/15.
 */
public class MainActivity extends BaseDoctorActivity {

    public static final int NOTFIRSTTIME = 2;
    public static final int ISFIRSTTIME = 1;
    public static final String DOCTOR_INDEX = "DOCTOR_INDEX";
    private ProfileModule api = Api.of(ProfileModule.class);
    private ActivityMainBinding binding;
    private RealmResults<TextMsg> unReadMsg;
    private boolean isShowing = false;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setFooter(getFooter());
        binding.setHandler(new MainActivityHandler(this));
        if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.DOCTOR_PASSED && Config.getInt(Constants.PASSFIRSTTIME, -1) == ISFIRSTTIME) {
            new PassDialog(this).show();
            Config.putInt(Constants.PASSFIRSTTIME, NOTFIRSTTIME);
        }
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

    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(this, R.id.tab_one);
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
}