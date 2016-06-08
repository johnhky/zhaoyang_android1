package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.entity.AfterService;
import com.doctor.sun.ui.fragment.EditForumFragment;
import com.doctor.sun.ui.fragment.ViewForumFragment;

/**
 * Created by rick on 6/6/2016.
 */
public class DoctorAfterServiceDonePA extends FragmentStatePagerAdapter {
    private String id;
    private EditForumFragment afterServiceForumFragment;

    public DoctorAfterServiceDonePA(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0: {
                return ViewForumFragment.newInstance(id, AfterService.TYPE.PATIENT);
            }
            case 1: {
                return ViewForumFragment.newInstance(id, AfterService.TYPE.DOCTOR);
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
        switch (position) {
            case 0: {
                return "患者问卷";
            }
            case 1: {
                return "医生建议";
            }
        }
        return "";
    }

}
