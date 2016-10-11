package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivitySettingBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.handler.SettingHandler;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import static io.ganguo.library.Config.clearData;

/**
 * Created by lucas on 12/21/15.
 */
public class SettingActivity extends BaseFragmentActivity2 implements SettingHandler.GetWindowSize {
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
        binding.setHandler(new SettingHandler());
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
}
