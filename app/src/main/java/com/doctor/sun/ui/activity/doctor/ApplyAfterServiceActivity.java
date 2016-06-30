package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityApplyAfterServiceBinding;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;

/**
 * Created by rick on 1/6/2016.
 */
public class ApplyAfterServiceActivity extends BaseActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    private ActivityApplyAfterServiceBinding binding;
    private Patient contact;
    private MultiSelectAdapter adapter;

    public static Intent intentFor(Context context, Patient contact) {
        Intent intent = new Intent(context, ApplyAfterServiceActivity.class);
        intent.putExtra(Constants.DATA, contact);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apply_after_service);
        initData();
        initHeader();
        initRecyclerView();
        initView();
    }

    private void initView() {
        binding.setConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> selectedRecords = getSelectedRecords();
                if (selectedRecords.isEmpty()) {
                    Toast.makeText(ApplyAfterServiceActivity.this, "请选择需要申请随访的病历", Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = JacksonUtils.toJson(selectedRecords);
                if (id != null) {
                    api.requestService(id).enqueue(new SimpleCallback<Void>() {
                        @Override
                        protected void handleResponse(Void response) {
                            Toast.makeText(ApplyAfterServiceActivity.this, "成功提交随访申请", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        binding.setSelectAllClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.getIsAllSelected()) {
                    adapter.unSelectAll();
                } else {
                    adapter.selectAll();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    private ArrayList<String> getSelectedRecords() {
        ArrayList<String> selectedRecords = new ArrayList<>();
        if (adapter != null) {
            for (int i = 0; i < adapter.size(); i++) {
                if (adapter.isSelected(i)) {
                    MedicalRecord o = (MedicalRecord) adapter.get(i);
                    if (o.canFollowUp.equals("1")) {
                        selectedRecords.add(String.valueOf(o.getMedicalRecordId()));
                    }
                }
            }
        }
        return selectedRecords;
    }

    private void initData() {
        contact = getIntent().getParcelableExtra(Constants.DATA);
        binding.setData(contact);
    }

    private void initHeader() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("患者信息");
        binding.setHeader(header);
    }

    private void initRecyclerView() {
        adapter = new MultiSelectAdapter(this, new MultiSelectAdapter.OnSelectionChange() {
            @Override
            public void onSelectionChange(BaseAdapter a, SparseBooleanArray selectedItems) {
                binding.setIsAllSelected(adapter.isAllSelected());
            }
        });
        adapter.mapLayout(R.layout.item_text, R.layout.item_select_record);
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                api.records(contact.getId()).enqueue(new ListCallback<MedicalRecord>(adapter));
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

}
