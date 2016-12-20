package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PMainActivity2Binding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class PMainActivity2 extends AppCompatActivity {

    private PMainActivity2Binding binding;

    private PushModule api = Api.of(PushModule.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_main_activity2);

        initRvMessage();
    }

    private void initRvMessage() {
        final SimpleAdapter adapter = new SimpleAdapter();
        api.systemMsg("1").enqueue(new SimpleCallback<PageDTO<SystemMsg>>() {
            @Override
            protected void handleResponse(PageDTO<SystemMsg> response) {
                // 只显示两条信息
                adapter.insert(response.getData().get(0));
                adapter.insert(response.getData().get(1));
                adapter.notifyDataSetChanged();
            }
        });
        adapter.mapLayout(R.layout.p_item_system_msg, R.layout.item_new_message);
        binding.rvMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMessage.setAdapter(adapter);
    }

    private void initDoctorView() {

    }
}
