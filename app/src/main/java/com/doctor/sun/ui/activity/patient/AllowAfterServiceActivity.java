package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAllowAfterServiceBinding;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 1/6/2016.
 */
public class AllowAfterServiceActivity extends BaseActivity2 {
    private ImModule imModule = Api.of(ImModule.class);
    private AfterServiceModule api = Api.of(AfterServiceModule.class);


    private ActivityAllowAfterServiceBinding binding;
    private Contact contact;
    private MultiSelectAdapter adapter;

    public static Intent intentFor(Context context, Contact contact) {
        Intent intent = new Intent(context, AllowAfterServiceActivity.class);
        intent.putExtra(Constants.DATA, contact);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_allow_after_service);
        initData();
        initHeader();
        initRecyclerView();
    }


    private void initData() {
        contact = getIntent().getParcelableExtra(Constants.DATA);
        imModule.doctorContact(contact.getDoctorId()).enqueue(new SimpleCallback<ContactDetail>() {
            @Override
            protected void handleResponse(ContactDetail response) {
                binding.doctorDetail.setData(response);
            }
        });
    }

    private void initHeader() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("医生详情");
        binding.setHeader(header);
    }

    private void initRecyclerView() {
        adapter = new MultiSelectAdapter(this, new MultiSelectAdapter.OnSelectionChange() {
            @Override
            public void onSelectionChange(BaseAdapter adapter, SparseBooleanArray selectedItems) {

            }
        });
        adapter.mapLayout(R.layout.item_text, R.layout.item_select_record2);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                imModule.recordDoctor(contact.getDoctorId()).enqueue(new ListCallback<MedicalRecord>(adapter));
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

}
