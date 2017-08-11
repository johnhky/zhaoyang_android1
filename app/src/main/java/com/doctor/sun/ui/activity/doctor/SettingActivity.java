package com.doctor.sun.ui.activity.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivitySettingBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Password;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.ChangePasswordFragment;
import com.doctor.sun.ui.fragment.SetPasswordFragment;
import com.doctor.sun.ui.handler.SettingHandler;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.DialogUtils;
import com.doctor.sun.util.UpdateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.ganguo.library.Config.clearData;

/**
 * Created by lucas on 12/21/15.
 */
public class SettingActivity extends BaseFragmentActivity2 implements SettingHandler.GetWindowSize {
    private ActivitySettingBinding binding;
    ProfileModule api = Api.of(ProfileModule.class);
    private boolean isSet;
    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, SettingActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PSW_UPDATE_SUCCESS);
        registerReceiver(receiver,filter);
        api.getPassword().enqueue(new Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                Gson gson = new GsonBuilder().create();
                if (null!=response){
                    Password data = gson.fromJson(response.body().getData().toString(), Password.class);
                    isSet = data.is_set_password();
                    if (data.is_set_password()==false){
                        binding.tvPassword.setText("设置密码");
                        binding.tvPsw.setVisibility(View.VISIBLE);
                    }else{
                        binding.tvPassword.setText("修改密码");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {

            }
        });
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setHandler(new SettingHandler());

        binding.llyPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSet==false){
                    Bundle bundle = SetPasswordFragment.getArgs();
                    Intent intent = SingleFragmentActivity.intentFor(v.getContext(),"设置密码",bundle);
                    v.getContext().startActivity(intent);
                }else{
                    Bundle bundle = ChangePasswordFragment.getArgs();
                    Intent intent = SingleFragmentActivity.intentFor(v.getContext(), "修改密码", bundle);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    private void initListener() {
        binding.llyCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoChoiceDialog.show(SettingActivity.this, "清理缓存", "取消", "清除", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(MaterialDialog dialog) {
                        clearData();
                        Toast.makeText(SettingActivity.this, "清理成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getWindowSize() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    @Override
    public String getMidTitleString() {
        return "设置";
    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent e) {
        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
    }
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.PSW_UPDATE_SUCCESS)) {
                binding.tvPassword.setText("修改密码");
                isSet = true;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
