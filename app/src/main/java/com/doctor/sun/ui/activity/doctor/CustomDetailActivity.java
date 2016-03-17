package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityCustomDetailBinding;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 12/26/15.
 */
public class CustomDetailActivity extends BaseActivity2 {
    private ActivityCustomDetailBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, CustomDetailActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_custom_detail);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("自编题目服务说明");
        binding.setHeader(header);
    }
}
