package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetailBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;;

import com.doctor.sun.ui.pager.DoctorDetailPagerAdapter;

/**
 * 医生详情
 * Created by rick on 20/1/2016.
 */
public class DoctorDetailActivity extends BaseFragmentActivity2{
    public static final String TAG = DoctorDetailActivity.class.getSimpleName();

    private ToolModule api = Api.of(ToolModule.class);
    private ActivityDoctorDetailBinding binding;
    private Doctor doctor;
    private AppointmentBuilder builder = new AppointmentBuilder();

    private FragmentPagerAdapter pagerAdapter;
    private AppointmentBuilder data;

    public static Intent makeIntent(Context context, Doctor data, int type) {
        Intent i = new Intent(context, DoctorDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.POSITION, type);
        return i;
    }


    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @AppointmentType
    private int getType() {
        //noinspection WrongConstant
        return getIntent().getIntExtra(Constants.POSITION, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctor = getData();
        builder.setDoctor(doctor);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail);
        binding.setType(getType());
        if (getType() == AppointmentType.PREMIUM) {
            binding.setTypeLabel("专属咨询");
        } else {
            binding.setTypeLabel("留言咨询");
        }
        binding.setData(getData());
        initData();
        data = new AppointmentBuilder();
        data.setDoctor(doctor);
        data.setType(getType());
        binding.duration.setData(data);
    }


    private void initData() {
        getDoctorInfo();
    }

    private void getDoctorInfo() {
        api.doctorInfo(getData().getId()).enqueue(new ApiCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                doctor = response;
                binding.setData(response);
                initPagerAdapter();
                initPagerTabs();
            }
        });
    }

    private void initPagerAdapter() {
        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);
        binding.vp.setOffscreenPageLimit(3);
    }

    private FragmentPagerAdapter createPagerAdapter() {
        return new DoctorDetailPagerAdapter(getSupportFragmentManager(), doctor);
    }

    private void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }
}
