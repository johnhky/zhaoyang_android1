package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetail2Binding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.event.SelectAppointmentTypeEvent;
import com.doctor.sun.model.DoctorDetailModel;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.ItemPickAppointmentDuration;
import com.doctor.sun.vm.ItemPremiumAppointment;
import com.doctor.sun.vm.ItemSpace;
import com.doctor.sun.vm.ItemStandardAppointment;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by kb on 13/12/2016.
 */

public class DoctorDetailActivity2 extends AppCompatActivity{

    private ActivityDoctorDetail2Binding binding;
    private SortedListAdapter adapter;
    private DoctorDetailModel model;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail2);
        adapter = new SortedListAdapter();
        model = new DoctorDetailModel();

        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back);
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back_blue);
                }
            }
        });
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
    }

    private void initRecyclerView() {
        Doctor data = getIntent().getParcelableExtra(Constants.DATA);
        List<SortedItem> items = model.parseData(data);
        adapter.insertAll(items);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.doctorDetail.setData(data);
        binding.rv.setAdapter(adapter);
    }

    public void appointment(View view) {
        SimpleAdapter adapter = new SimpleAdapter();
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("预约类型");
        builder.titleGravity(GravityEnum.CENTER);
        adapter.add(new ItemPremiumAppointment());
        adapter.add(new ItemSpace());
        adapter.add(new ItemStandardAppointment());
        dialog = builder.adapter(adapter, new LinearLayoutManager(this)).build();
        dialog.show();
    }

    @Subscribe
    public void onSelectAppointmentTypeEvent(SelectAppointmentTypeEvent event) {
        Log.e("KB", "Test");

        dialog.dismiss();
        adapter.clear();

        adapter.insert(new ItemPickAppointmentDuration());
    }
}
