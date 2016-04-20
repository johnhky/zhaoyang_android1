package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorDetailBinding;
import com.doctor.sun.databinding.DialogPickDurationBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.DoctorPageCallback;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.SearchDoctorAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.DoctorDetailPagerAdapter;

/**
 * 医生详情
 * Created by rick on 20/1/2016.
 */
public class DoctorDetailActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    public static final String TAG = DoctorDetailActivity.class.getSimpleName();

    private ToolModule api = Api.of(ToolModule.class);
    private ActivityDoctorDetailBinding binding;
    private HeaderViewModel headerViewModel;
    private Doctor doctor;

    private FragmentPagerAdapter pagerAdapter;

    public static Intent makeIntent(Context context, Doctor data, int type) {
        Intent i = new Intent(context, DoctorDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.POSITION, type);
        return i;
    }


    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Appointment.Type
    private int getType() {
        //noinspection WrongConstant
        return getIntent().getIntExtra(Constants.POSITION, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctor = getData();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_detail);
        binding.setType(getType());
        if (getType() == Appointment.DETAIL) {
            binding.setTypeLabel("详细咨询");
        } else {
            binding.setTypeLabel("简捷复诊");
        }
        binding.setData(getData());
        headerViewModel = new HeaderViewModel(this);
        HeaderViewModel header = headerViewModel;
        header.setMidTitle("医生详情").setRightIcon(R.drawable.ic_not_like_doctor);
        binding.setHeader(header);
        initData();
        binding.tvHosptial.setOnClickListener(this);
        initDurationPicker();

        initPagerAdapter();
        initPagerTabs();
    }

    private void initDurationPicker() {

        final DialogPickDurationBinding binding = this.binding.duration;
        binding.setMoney(0);
        if (getType() == Appointment.QUICK) {
            for (int i = 2; i < binding.rgDuration.getChildCount(); i++) {
                binding.rgDuration.getChildAt(i).setVisibility(View.GONE);
            }
        }
        binding.rgDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedItem = -1;
                for (int i = 1; i < binding.rgDuration.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) binding.rgDuration.getChildAt(i);
                    if (childAt.isChecked()) {
                        selectedItem = i;
                    }
                }
                if (getType() == Appointment.QUICK) {
                    binding.setMoney(doctor.getSecondMoney() * (selectedItem));
                } else {
                    binding.setMoney(doctor.getMoney() * (selectedItem));
                }
            }
        });
        binding.tvPickDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = -1;
                for (int i = 1; i < binding.rgDuration.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) binding.rgDuration.getChildAt(i);
                    if (childAt.isChecked()) {
                        selectedItem = i;
                    }
                }

                if (selectedItem != -1) {
                    doctor.setDuration(String.valueOf((selectedItem) * 15));
                    doctor.getHandler().pickDate().onItemClick(new SearchDoctorAdapter(DoctorDetailActivity.this, getType()), binding.tvPickDuration, null);
                } else {
                    Toast.makeText(DoctorDetailActivity.this, "请选择时长", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initData() {
        getDoctorInfo();
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (doctor != null) {
            if (doctor.getIsFav().equals("1")) {
                api.unlikeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        //改状态,不然下次点击还是会跑到这里,但是医生已经取消收藏了.
                        setIsFav("0", R.drawable.ic_not_like_doctor, headerViewModel);
                        Toast.makeText(DoctorDetailActivity.this, "取消收藏医生", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                api.likeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        //改状态,不然下次点击还是会跑到这里,但是医生已经收藏过了.
                        setIsFav("1", R.drawable.ic_like_doctor, headerViewModel);
                        Toast.makeText(DoctorDetailActivity.this, "成功收藏医生", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void getDoctorInfo() {
        api.doctorInfo(getData().getId()).enqueue(new ApiCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                doctor = response;
                if (response.getIsFav().equals("1")) {
                    headerViewModel.setRightIcon(R.drawable.ic_like_doctor);
                } else {
                    headerViewModel.setRightIcon(R.drawable.ic_not_like_doctor);
                }
                binding.setData(response);
                binding.setHeader(headerViewModel);
            }
        });
    }

    private void setIsFav(String isFav, int ic_not_like_doctor, HeaderViewModel headerViewModel) {
        doctor.setIsFav(isFav);
        headerViewModel.setRightIcon(ic_not_like_doctor);
        binding.setHeader(headerViewModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hosptial:
                Intent intent = HospitalDetailActivity.makeIntent(this, getData());
                startActivity(intent);
                break;
        }
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
