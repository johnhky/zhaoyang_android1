package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.fragment.PickDateFragment;

/**
 * Created by rick on 12/17/15.
 */
public class PickDatePagerAdapter extends FragmentPagerAdapter {


    private final AppointmentBuilder data;

    public PickDatePagerAdapter(FragmentManager supportFragmentManager, AppointmentBuilder data) {
        super(supportFragmentManager);
        this.data = data;
    }

    @Override
    public Fragment getItem(final int position) {
        PickDateFragment fragment = PickDateFragment.newInstance(data);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (data.getType() == AppointmentType.PREMIUM) {
            return "专属咨询";
        } else {
            return "留言咨询";
        }
    }

}
