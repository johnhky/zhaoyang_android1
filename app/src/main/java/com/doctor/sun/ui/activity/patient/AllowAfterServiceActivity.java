package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAllowAfterServiceBinding;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.AllowAfterServiceAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 1/6/2016.
 */
public class AllowAfterServiceActivity extends BaseActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    private Doctor contact;
    private ActivityAllowAfterServiceBinding binding;
    private AllowAfterServiceAdapter adapter;

    public static Intent intentFor(Context context, Doctor contact) {
        Intent intent = new Intent(context, AllowAfterServiceActivity.class);
        intent.putExtra(Constants.DATA, contact);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_allow_after_service);
        contact = getIntent().getParcelableExtra(Constants.DATA);
        initHeader();
        initRecyclerView();
        initData();
    }


    private void initData() {
        api.doctorInfo(contact.getId(), "follow_up").enqueue(new SimpleCallback<ContactDetail>() {
            @Override
            protected void handleResponse(ContactDetail response) {
                binding.doctorDetail.setData(response);
                if (adapter == null) {
                    return;
                }
                adapter.addAll(response.getRecords());
                adapter.onFinishLoadMore(true);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initHeader() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("医生详情");
        binding.setHeader(header);
    }

    private void initRecyclerView() {
        adapter = new AllowAfterServiceAdapter(this, contact.getId());
        adapter.onFinishLoadMore(true);
        adapter.mapLayout(R.layout.item_text, R.layout.item_select_record2);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

}
