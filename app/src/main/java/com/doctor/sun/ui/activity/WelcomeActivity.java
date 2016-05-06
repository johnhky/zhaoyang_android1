package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityWelcomeBinding;

/**
 * Created by rick on 16/2/2016.
 */
public class WelcomeActivity extends BaseFragmentActivity2 {

    private ActivityWelcomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        binding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = LoginActivity.makeIntent(WelcomeActivity.this);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }


    @Override
    protected boolean shouldCheck() {
        return false;
    }
}
