package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PActivityMainBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.patient.handler.MainActivityHandler;
import com.doctor.sun.ui.adapter.SearchDoctorAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.ui.widget.AddMedicalRecordDialog;


/**
 * Created by rick on 10/23/15.
 */
public class MainActivity extends BaseActivity2 {

    private PActivityMainBinding binding;
    private SimpleAdapter mAdapter;
    private ToolModule api = Api.of(ToolModule.class);
    private ListCallback<Doctor> callback;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_main);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("昭阳医生");
        binding.setHeader(header);
        PatientFooterView mView = new PatientFooterView(this);
        FooterViewModel footer =FooterViewModel.getInstance(mView, realm, R.id.tab_one);
        binding.setFooter(footer);

        final LinearLayoutManager layout = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layout);
        mAdapter = createAdapter();
        callback = new ListCallback<Doctor>(mAdapter);
        binding.recyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        Patient patientProfile = TokenCallback.getPatientProfile();
        if (patientProfile.getName().equals("")) {
            new AddMedicalRecordDialog(this).show();
        }

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstCompletelyVisibleItemPosition = layout.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition > 1) {
                    binding.llyLabelHeader.setVisibility(View.VISIBLE);
                } else {
                    binding.llyLabelHeader.setVisibility(View.GONE);
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    protected void loadMore() {
        mAdapter.add(new MainActivityHandler(this));
        mAdapter.add(new LayoutId() {
            @Override
            public int getItemLayoutId() {
                return R.layout.header_recommand_doctor;
            }
        });
        api.recommendDoctor().enqueue(callback);
    }

    protected SimpleAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected SimpleAdapter createAdapter() {
        SimpleAdapter simpleAdapter = new SearchDoctorAdapter(this, Appointment.DETAIL);
        simpleAdapter.mapLayout(R.layout.item_doctor, R.layout.item_recommand_doctor);
        return simpleAdapter;
    }
}
