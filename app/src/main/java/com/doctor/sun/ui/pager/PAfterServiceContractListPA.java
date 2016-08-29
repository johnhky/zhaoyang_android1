package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.ui.fragment.PAfterServiceContactFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

/**
 * Created by rick on 1/3/2016.
 */
public class PAfterServiceContractListPA extends FragmentStatePagerAdapter {

    public PAfterServiceContractListPA(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0: {
                fragment = PAfterServiceContactFragment.newInstance("follow");
                break;
            }
            case 1: {
                fragment = PAfterServiceContactFragment.newInstance("unfollow");
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
        switch (position) {
            case 0: {
                return "已建立随访关系";
            }
            case 1: {
                return "未建立随访关系";
            }
        }
        return "";
    }


}
