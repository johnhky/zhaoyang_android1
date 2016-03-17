package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.ui.fragment.ListFragment;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.PUrgentCallFragment;

/**
 * Created by rick on 12/17/15.
 */
public class PAppointmentPagerAdapter extends FragmentPagerAdapter {

    public PAppointmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        ListFragment fragment = null;
        switch (position) {
            case 0: {
                fragment = PAppointmentListFragment.newInstance(1);
                break;
            }
            case 1: {
                fragment = PUrgentCallFragment.getInstance();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "我的预约";
        } else {
            return "紧急咨询";
        }
    }

}
