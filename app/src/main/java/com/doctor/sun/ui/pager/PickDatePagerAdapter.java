package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.fragment.PickDateFragment;

/**
 * Created by rick on 12/17/15.
 */
public class PickDatePagerAdapter extends FragmentPagerAdapter {

    private final Doctor doctor;
    private int type;

    public PickDatePagerAdapter(FragmentManager fm, Doctor doctor, int type) {
        super(fm);
        this.doctor = doctor;
        this.type = type;
    }

    @Override
    public Fragment getItem(final int position) {
        PickDateFragment fragment = PickDateFragment.newInstance(doctor, type);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (type == AppointmentType.DETAIL) {
            return "详细就诊";
        } else {
            return "简捷复诊";
        }
    }

}
