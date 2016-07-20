package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PActivityAppointmentTypeBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.TextViewFragment;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.util.Systems;

/**
 * Created by rick on 14/7/2016.
 */

public class SelectAppointmentTypeActivity extends BaseFragmentActivity2 {


    private static final int[] TEXT_ID = new int[]{
            R.string.premium_product_brief,
            R.string.normal_product_brief};

    private PActivityAppointmentTypeBinding binding;
    private AppointmentBuilder data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_appointment_type);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("预约医生");
        binding.headerLayout.setHeader(header);
        int color = Color.parseColor("#8dc63f");
        binding.headerLayout.header.setBackgroundColor(color);
        Systems.setStatusColor(SelectAppointmentTypeActivity.this, color);
        data = new AppointmentBuilder();
        data.setType(AppointmentType.DETAIL);
        binding.setData(data);
        binding.vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TextViewFragment.newInstance(getResources().getString(TEXT_ID[position]));
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        binding.premium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setCurrentItem(b);
                if (b) {
                    int color = Color.parseColor("#8dc63f");
                    binding.headerLayout.header.setBackgroundColor(color);
                    Systems.setStatusColor(SelectAppointmentTypeActivity.this, color);
                }
            }
        });
        binding.normal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setCurrentItem(!b);
                if (b) {
                    int color = getResources().getColor(R.color.color_normal_selected);
                    binding.headerLayout.header.setBackgroundColor(color);
                    Systems.setStatusColor(SelectAppointmentTypeActivity.this, color);
                }
            }
        });

        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.getData().setType(AppointmentType.DETAIL);
                } else if (position == 1) {
                    binding.getData().setType(AppointmentType.QUICK);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCurrentItem(boolean b) {
        if (b) {
            binding.vp.setCurrentItem(0);
        } else {
            binding.vp.setCurrentItem(1);
        }
    }

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, SelectAppointmentTypeActivity.class);
        return intent;
    }
}
