package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityTimeBinding;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.module.impl.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.TimeAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.Function0;

import java.util.List;

import io.ganguo.library.common.ToastHelper;

/**
 * 出诊时间
 * <p/>
 * Created by lucas on 12/1/15.
 */
public class TimeActivity extends BaseActivity2 implements TimeHandler.GetIsEditMode {

    private HeaderViewModel header = new HeaderViewModel(this);

    private TimeAdapter mAdapter;
    private ActivityTimeBinding binding;
    private TimeModuleWrapper api = TimeModuleWrapper.getInstance();

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, TimeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }


    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time);
        header.setLeftIcon(R.drawable.ic_back)
                .setMidTitle("出诊时间")
                .setRightTitle("编辑");
        binding.setHeader(header);
        mAdapter = new TimeAdapter(this);
        binding.rvTime.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTime.setAdapter(mAdapter);
        binding.setHandler(new TimeHandler());
    }

    private void initData() {
        mAdapter.onFinishLoadMore(true);
        api.getAllTimeAsync(new Function0<List<LayoutId>>() {
            @Override
            public void apply(List<LayoutId> r) {
                mAdapter.clear();
                mAdapter.addAll(r);
                mAdapter.notifyDataSetChanged();
                mAdapter.onFinishLoadMore(true);
            }
        });
    }

    @Override
    public void onBackClicked() {
        if (mAdapter.isEditMode()) {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            header.setRightTitle("编辑");
            binding.llAdd.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else super.onBackClicked();
        binding.setHeader(header);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (mAdapter.getItemCount() == 0) {
            ToastHelper.showMessage(this, "目前没有出诊时间安排");
            header.setRightTitle("编辑");
            binding.llAdd.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            if (mAdapter.isEditMode()) {
                header.setRightTitle("保存");
                binding.llAdd.setVisibility(View.GONE);
            } else {
                header.setRightTitle("编辑");
                binding.llAdd.setVisibility(View.VISIBLE);
            }
        }
        binding.setHeader(header);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }

}