package com.doctor.sun.ui.activity.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityTimeBinding;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.vm.LayoutId;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.util.Function0;

import java.util.List;

import static android.view.View.VISIBLE;

/**
 * 出诊时间
 * <p/>
 * Created by lucas on 12/1/15.
 */
public class TimeActivity extends BaseFragmentActivity2 implements TimeHandler.GetIsEditMode {

    private SimpleAdapter adapter;
    private ActivityTimeBinding binding;
    private TimeModuleWrapper api = TimeModuleWrapper.getInstance();
    private IntentFilter getBorad = new IntentFilter();
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
        adapter = new SimpleAdapter();
        adapter.putBoolean(AdapterConfigKey.IS_EDIT_MODE, false);
        binding.rvTime.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTime.setAdapter(adapter);
        binding.setHandler(new TimeHandler());
        getBorad.addAction("toFinish");
        registerReceiver(broadcastReceiver,getBorad);
    }

    private void initData() {
        adapter.onFinishLoadMore(true);
        api.getAllTimeAsync(new Function0<List<LayoutId>>() {
            @Override
            public void apply(List<LayoutId> r) {
                adapter.clear();
                adapter.addAll(r);
                adapter.notifyDataSetChanged();
                adapter.onFinishLoadMore(true);
                if (adapter.isEmpty()) {
                    binding.emptyIndicator.setText("您还没有设置任何出诊时间");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.INVISIBLE);
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackPressed() {
        boolean isEditMode = adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE);
        if (isEditMode) {
            adapter.putBoolean(AdapterConfigKey.IS_EDIT_MODE, false);
            binding.llAdd.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    public void onMenuClicked() {
        if (adapter.getItemCount() == 0) {
            Toast.makeText(this, "目前没有出诊时间安排", Toast.LENGTH_SHORT).show();
            binding.llAdd.setVisibility(VISIBLE);
        } else {
            adapter.putBoolean(AdapterConfigKey.IS_EDIT_MODE, !adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE));
            if (adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE)) {
                binding.llAdd.setVisibility(View.GONE);
            } else {
                binding.llAdd.setVisibility(View.VISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean getIsEditMode() {
        return adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        adapter.putBoolean(AdapterConfigKey.IS_EDIT_MODE, adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE));
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
        if (adapter.getItemCount() == 0) {
            return false;
        }
        MenuInflater menuInflater = getMenuInflater();
        if (adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE)) {
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

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                    if (intent.getAction()=="toFinish"){
                        finish();
                    }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}