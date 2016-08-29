package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.ui.fragment.PAfterServiceFragment;
import com.doctor.sun.ui.fragment.PApplyingDoctorListFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

/**
 * Created by rick on 1/3/2016.
 */
public class PatientRelationshipListPA extends FragmentStatePagerAdapter {

    public PatientRelationshipListPA(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0: {
                fragment = PAfterServiceFragment.newInstance();
                break;
            }
            case 1: {
                fragment = PApplyingDoctorListFragment.newInstance();
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
                return "随访订单";
            }
            case 1: {
                return "关系申请";
            }
        }
        return "";
    }


}
