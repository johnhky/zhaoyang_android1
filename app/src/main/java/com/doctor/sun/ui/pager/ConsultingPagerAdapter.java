package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.ui.fragment.ConsultedFragment;
import com.doctor.sun.ui.fragment.ConsultingFragment;

/**
 * Created by rick on 12/17/15.
 */
public class ConsultingPagerAdapter extends FragmentPagerAdapter {


    public ConsultingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0: {
                return new ConsultingFragment();
            }
            case 1: {
                return new ConsultedFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "进行中";
        } else {
            return "已完成";
        }
    }
}
