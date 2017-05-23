package com.doctor.sun.ui.activity.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.ActivityTimeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.AllTime;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.TimeAdapter;
import com.doctor.sun.vm.LayoutId;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.util.Function0;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

/**
 * 出诊时间
 * <p/>
 * Created by lucas on 12/1/15.
 */
public class TimeActivity extends BaseFragmentActivity2 {


    private ActivityTimeBinding binding;
    private TimeModule api = Api.of(TimeModule.class);
    private IntentFilter getBorad = new IntentFilter();
    private TimeAdapter mNetAdapter;
    private TimeAdapter mFaceAdapter;
    boolean isShow = false;

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
        binding.setHandler(new TimeHandler());
        Doctor doctor = Settings.getDoctorProfile();
        binding.tvAddress.setText("诊所地址:" + doctor.getClinicAddress().getAddress());

        if (doctor.getSpecialistCateg() == 1) {
            binding.llSimple.setVisibility(View.GONE);
        }
        getBorad.addAction("toFinish");
        registerReceiver(broadcastReceiver, getBorad);
    }
    public void getTime(){

    }

    private void initData() {
        api.getNewTime().enqueue(new SimpleCallback<AllTime>() {
            @Override
            protected void handleResponse(AllTime response) {
                mNetAdapter = new TimeAdapter(TimeActivity.this, response.netWork, binding.getRoot());
                mFaceAdapter = new TimeAdapter(TimeActivity.this, response.surface, binding.getRoot());
                binding.rvTime.setAdapter(mNetAdapter);
                binding.rvSurfaceTime.setAdapter(mFaceAdapter);
                if (response.netWork.size() > 0) {
                    binding.llNet.setVisibility(View.VISIBLE);
                    binding.rvTime.setVisibility(View.VISIBLE);
                }
                if (response.surface.size() > 0) {
                    binding.llSurface.setVisibility(View.VISIBLE);
                    binding.rvSurfaceTime.setVisibility(View.VISIBLE);
                }
                if (response.simple.size() > 0) {
                    binding.btnSimple.setChecked(true);
                    binding.tvSimple.setText("简易复诊(开启)");
                }
                if (mNetAdapter.isEmpty() && mFaceAdapter.isEmpty()) {
                    binding.emptyIndicator.setText("您还没有设置任何出诊时间");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.INVISIBLE);
                }
                invalidateOptionsMenu();
            }

        });
        binding.btnSimple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked==true){
                    api.setSimple().enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            binding.btnSimple.setChecked(false);
                            binding.tvSimple.setText("简易复诊(关闭)");
                            Toast.makeText(TimeActivity.this, "闲时咨询已关闭!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    api.setSimple().enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            binding.btnSimple.setChecked(true);
                            binding.tvSimple.setText("简易复诊(开启)");
                            Toast.makeText(TimeActivity.this, "闲时咨询已开启!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isShow) {
            binding.llAdd.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public void onMenuClicked() {
        if (isShow) {
            binding.llAdd.setVisibility(View.VISIBLE);
        } else {
            binding.llAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFaceAdapter != null && mFaceAdapter != null) {
            getMenuInflater().inflate(R.menu.menu_edit_save, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                isShow = true;
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
            case R.id.action_save: {
                isShow = false;
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
        MenuInflater inflater = getMenuInflater();
        if (isShow) {
            inflater.inflate(R.menu.menu_edit, menu);
        } else {
            inflater.inflate(R.menu.menu_save, menu);
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
            if (intent.getAction() == "toFinish") {
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