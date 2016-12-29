package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.constans.QuestionsType;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

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
                return ReadQuestionsFragment.getInstance(id,
                        QuestionsPath.FOLLOW_UP,
                        QuestionsType.PATIENT_W_PATIENT_QUESTIONS,
                        true);
            }
            case 1: {
                return ReadQuestionsFragment.getInstance(id,
                        QuestionsPath.FOLLOW_UP,
                        QuestionsType.PATIENT_R_DOCTOR_QUESTIONS,
                        true);
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
