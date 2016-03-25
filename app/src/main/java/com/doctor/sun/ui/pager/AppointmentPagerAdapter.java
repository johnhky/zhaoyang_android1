package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.ui.fragment.AppointmentListFragment;
import com.doctor.sun.ui.fragment.ListFragment;

/**
 * Created by rick on 12/17/15.
 */
public class AppointmentPagerAdapter extends FragmentPagerAdapter {
    public static final int[] STATUS = new int[]{AppointMent.PAID, AppointMent.NOT_PAID};

    public AppointmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        ListFragment fragment = AppointmentListFragment.getInstance(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "已付款";
        } else {
            return "未付款";
        }
    }

}
