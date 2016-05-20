package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.ui.fragment.CouponsFragment;


/**
 * Created by rick on 20/5/2016.
 */
public class CouponPagerAdapter extends FragmentPagerAdapter {


    public CouponPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0: {
                return CouponsFragment.newInstance("valid");
            }
            case 1: {
                return CouponsFragment.newInstance("invalid");
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
            return "可使用";
        } else {
            return "已失效";
        }
    }
}
