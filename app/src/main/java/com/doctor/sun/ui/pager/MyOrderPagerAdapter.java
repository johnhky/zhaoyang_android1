package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.fragment.ListFragment;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.PUrgentCallFragment;

/**
 * Created by rick on 1/3/2016.
 */
public class MyOrderPagerAdapter extends FragmentPagerAdapter {

    public MyOrderPagerAdapter(FragmentManager fm) {
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
                fragment = DrugListFragment.getInstance();
                break;
            }
            case 2: {
                fragment = PUrgentCallFragment.getInstance();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "咨询订单";
            }
            case 1: {
                return "寄药订单";
            }
            case 2: {
                return "紧急咨询订单";
            }
        }
        return "";
    }

}
