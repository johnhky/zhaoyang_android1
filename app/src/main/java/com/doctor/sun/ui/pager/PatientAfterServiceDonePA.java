package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.entity.AfterService;
import com.doctor.sun.ui.fragment.DoctorSugestionFragment;
import com.doctor.sun.ui.fragment.EditForumFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;
import com.doctor.sun.ui.fragment.ViewForumFragment;

/**
 * Created by rick on 6/6/2016.
 */
public class PatientAfterServiceDonePA extends FragmentStatePagerAdapter {
    private String id;

    public PatientAfterServiceDonePA(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(final int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0: {
                return ViewForumFragment.newInstance(id, AfterService.TYPE.PATIENT);
            }
            case 1: {
                return DoctorSugestionFragment.newInstance(id, AfterService.TYPE.PATIENT);
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
                return "患者问卷";
            }
            case 1: {
                return "医生记录";
            }
        }
        return "";
    }

}
