package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivitySettingBinding;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.handler.SettingHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/21/15.
 */
public class SettingActivity extends BaseActivity2 implements SettingHandler.GetWindowSize {
    private ActivitySettingBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, SettingActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("设置");
        binding.setHeader(header);
        binding.setHandler(new SettingHandler(SettingActivity.this));
    }

    private void initListener() {
        binding.llyCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSelectorDialog.showTwoSelectorDialog(SettingActivity.this, "清理缓存", "取消", "清除", new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(TwoSelectorDialog dialog) {
                        Config.clearData();
                        ToastHelper.showMessage(SettingActivity.this, "清理成功");
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
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
}
