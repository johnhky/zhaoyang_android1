package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAdviceDetailBinding;
import com.doctor.sun.entity.Advice;

/**
 */
public class AdviceDetailActivity extends BaseFragmentActivity2 {
    private ActivityAdviceDetailBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advice_detail);
        binding.setData(getData());
    }

    private Advice getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }


    @Override
    public int getMidTitle() {
        return R.string.title_advice_history;
    }

    public static Intent intentFor(Context context, Advice advice) {
        Intent intent = new Intent(context, AdviceDetailActivity.class);
        intent.putExtra(Constants.DATA, advice);
        return intent;
    }

    public static void startFrom(Context context, Advice advice) {
        context.startActivity(intentFor(context, advice));
    }
}