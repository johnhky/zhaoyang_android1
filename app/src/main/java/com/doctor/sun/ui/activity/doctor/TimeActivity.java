package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityTimeBinding;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.TimeAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.Function0;

import java.util.List;

import io.ganguo.library.common.ToastHelper;

/**
 * 出诊时间
 * <p/>
 * Created by lucas on 12/1/15.
 */
public class TimeActivity extends BaseFragmentActivity2 implements TimeHandler.GetIsEditMode {


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
                if (mAdapter.isEmpty()) {
                    binding.emptyIndicator.setText("您还没有设置任何出诊时间");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isEditMode()) {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            binding.llAdd.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    public void onMenuClicked() {
        if (mAdapter.getItemCount() == 0) {
            ToastHelper.showMessage(this, "目前没有出诊时间安排");
            binding.llAdd.setVisibility(View.VISIBLE);
        } else {
//            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            if (mAdapter.isEditMode()) {
                binding.llAdd.setVisibility(View.GONE);
            } else {
                binding.llAdd.setVisibility(View.VISIBLE);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAdapter.setIsEditMode(!mAdapter.isEditMode());
        switch (item.getItemId()) {
            case R.id.action_edit: {
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
            case R.id.action_save: {
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        if (mAdapter.getItemCount() == 0) {
            return false;
        }
        MenuInflater menuInflater = getMenuInflater();
        if (mAdapter.isEditMode()) {
            menuInflater.inflate(R.menu.menu_save, menu);
        } else {
            menuInflater.inflate(R.menu.menu_edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_times;
    }
}