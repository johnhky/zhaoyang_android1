package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityHelpBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.pager.HelpAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucas on 2/2/16.
 */
public class HelpActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    private HelpAdapter mAdapter;
    private ActivityHelpBinding binding;
    private ToolModule api = Api.of(ToolModule.class);

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
        String type = "";
        if (Settings.isDoctor()) {
            type = "doctor";
        } else {
            type = "patient";
        }
        api.helpImage("ios", type).enqueue(new Callback<ApiDTO<List<String>>>() {
            @Override
            public void onResponse(Call<ApiDTO<List<String>>> call, Response<ApiDTO<List<String>>> response) {
                mAdapter = new HelpAdapter(getSupportFragmentManager(), response.body().getData());
                binding.vp.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
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
    public void onClick(View v) {
        if (v.getId() == R.id.iv_exit) {
            finish();
        }
    }
}
