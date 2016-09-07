package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;

/**
 * Created by lucas on 1/8/16.
 */
public class HistoryDetailAdapter extends FragmentPagerAdapter {
    private Appointment appointment;

    public HistoryDetailAdapter(FragmentManager fm, Appointment appointment) {
        super(fm);
        this.appointment = appointment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ReadQuestionsFragment.getInstance(appointment.getIdString(), QuestionsPath.NORMAL, true);
            case 1:
                return ReadDiagnosisFragment.newInstance(appointment.getId());
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
