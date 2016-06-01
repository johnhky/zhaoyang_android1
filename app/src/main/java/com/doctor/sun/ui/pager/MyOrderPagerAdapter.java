package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.PUrgentCallFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

/**
 * Created by rick on 1/3/2016.
 */
public class MyOrderPagerAdapter extends FragmentPagerAdapter {

    public MyOrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        RefreshListFragment fragment = null;
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
            case 3: {
                fragment = PAppointmentListFragment.newInstance(1);
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
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
                return "紧急咨询";
            }
            case 3: {
                return "随访订单";
            }
        }
        return "";
    }

}
