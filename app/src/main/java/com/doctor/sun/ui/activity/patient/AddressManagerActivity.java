package com.doctor.sun.ui.activity.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityAddressListBinding;
import com.doctor.sun.entity.Address;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.AddressAdapter;

import java.util.List;

/**
 * Created by heky on 17/4/26.
 */

public class AddressManagerActivity extends BaseFragmentActivity2 {

    private PActivityAddressListBinding binding;
    private AddressAdapter myAdapter;
    private ProfileModule api = Api.of(ProfileModule.class);
    private Intent getData;
    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AddressManagerActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("delete");
        intentFilter.addAction("addAddress");
        intentFilter.addAction("updateSuccess");
        getData = getIntent();
        registerReceiver(receiver,intentFilter);
        initView();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    public void initListener(){
        binding.tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent toAdd = SingleFragmentActivity.intentFor(AddressManagerActivity.this,
                    "添加新地址",AddressAddFragment.upload());
                startActivity(toAdd);
            }
        });
    }

    public void initData() {
        api.getAddressList().enqueue(new SimpleCallback<List<Address>>() {
            @Override
            protected void handleResponse(List<Address> response) {
                myAdapter = new AddressAdapter(response);
                myAdapter.notifyDataSetChanged();
                if (response.isEmpty()){
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                }else{
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
                binding.rvList.setAdapter(myAdapter);
            }
        });
    }


    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_address_list);
        binding.rvList.setLayoutManager(new LinearLayoutManager(this));

    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("delete")){
                initData();
            }else if (intent.getAction().equals("addAddress")){
                initData();
            }else if (intent.getAction().equals("updateSuccess")){
                if (getData!=null){
                    if (null!=getData.getStringExtra(Constants.MOCK)){
                        finish();
                    }
                }

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int getMidTitle() {
        return R.string.address_manager;
    }
}
