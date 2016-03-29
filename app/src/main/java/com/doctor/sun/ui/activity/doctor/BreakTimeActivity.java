package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityBreakTimeBinding;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.BreakTimeAdapter;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.ui.model.HeaderViewModel;

import java.util.List;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by lucas on 12/3/15.
 */
public class BreakTimeActivity extends BaseActivity2 implements TimeHandler.GetIsEditMode {
    private final static Logger LOG = LoggerFactory.getLogger(BreakTimeActivity.class);
    public static final int ADD_BREAK_TIME = 1;

    private HeaderViewModel header = new HeaderViewModel(this);

    private BreakTimeAdapter mAdapter;
    private ActivityBreakTimeBinding binding;
    private TimeModule api = Api.of(TimeModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, BreakTimeActivity.class);
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_break_time);
        header.setLeftIcon(R.drawable.ic_back).setMidTitle("免打扰").setRightTitle("编辑");
        binding.setHeader(header);
        mAdapter = new BreakTimeAdapter(this);
        mAdapter.mapLayout(R.layout.item_time, R.layout.item_break_time);
        binding.rvDisturb.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDisturb.setAdapter(mAdapter);
        binding.setHandler(new TimeHandler());
    }

    private void initData() {
        api.getTime(Time.TYPE_BREAK).enqueue(new SimpleCallback<List<Time>>() {
            @Override
            protected void handleResponse(List<Time> response) {
                mAdapter.clear();
                mAdapter.addAll(response);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                super.onFailure(t);
                mAdapter.onFinishLoadMore(true);
            }
        });
    }

    @Override
    public void onBackClicked() {
        binding.setHeader(header);
        if (mAdapter.isEditMode()) {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            header.setRightTitle("编辑");
            binding.llAdd.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackClicked();
        }
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (mAdapter.getItemCount() == 0) {
            ToastHelper.showMessage(this, "目前没有免打扰时间");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ADD_BREAK_TIME){
            mAdapter.add(data);
        }
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }

}
