package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityHelpBinding;
import com.doctor.sun.ui.adapter.HelpAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 2/2/16.
 */
public class HelpActivity extends TabActivity implements View.OnClickListener {
    private HelpAdapter mAdapter;
    private ActivityHelpBinding binding;

    public static Intent makeIntent(Context context, int type) {
        Intent i = new Intent(context, HelpActivity.class);
        i.putExtra(Constants.TYPE, type);
        return i;
    }

    private int getType() {
        return getIntent().getIntExtra(Constants.TYPE, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        binding.ivExit.setOnClickListener(this);
        mAdapter = createPagerAdapter();
        binding.vp.setAdapter(mAdapter);
    }

    private void initListener() {
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (binding.vp.getCurrentItem() == binding.vp.getAdapter().getCount() - 1) {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected HelpAdapter createPagerAdapter() {
        return new HelpAdapter(getSupportFragmentManager(), getType());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_exit) {
            finish();
        }
    }
}
