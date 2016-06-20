package com.doctor.sun.ui.pager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.entity.AfterService;
import com.doctor.sun.ui.fragment.DoctorSugestionFragment;
import com.doctor.sun.ui.fragment.EditForumFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

/**
 * Created by rick on 6/6/2016.
 */
public class PatientAfterServicePA extends FragmentStatePagerAdapter {
    private String id;
    private EditForumFragment afterServiceForumFragment;

    public PatientAfterServicePA(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(final int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0: {
                afterServiceForumFragment = EditForumFragment.newInstance(id, AfterService.TYPE.PATIENT);
                fragment = afterServiceForumFragment;
                break;
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

    public void saveAnswer() {
        if (afterServiceForumFragment != null) {
            afterServiceForumFragment.saveAnswer();
        }
    }
    public void handleImageResult(final int requestCode, int resultCode, Intent data) {
        if (afterServiceForumFragment != null) {
            afterServiceForumFragment.handleImageResult(requestCode, resultCode, data);
        }
    }
}
