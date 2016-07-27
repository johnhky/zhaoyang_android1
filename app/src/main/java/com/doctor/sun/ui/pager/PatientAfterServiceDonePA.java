package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.ui.fragment.DoctorSuggestionFragment;
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
                return DoctorSuggestionFragment.newInstance(id, AfterService.TYPE.PATIENT);
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
        if (!Settings.isDoctor()) {
            if (position == 0) {
                return "我的问卷";
            } else {
                return "医生建议";
            }
        } else {
            if (position == 0) {
                return "患者问卷";
            } else {
                return "病历记录";
            }
        }
    }

}
