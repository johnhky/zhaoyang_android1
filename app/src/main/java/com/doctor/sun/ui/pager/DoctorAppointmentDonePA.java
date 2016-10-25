package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;

/**
 * Created by rick on 1/8/2016.
 */

public class DoctorAppointmentDonePA extends FragmentStatePagerAdapter {

    String appointmentId;


    public DoctorAppointmentDonePA(FragmentManager fm, String appointmentId) {
        super(fm);
        this.appointmentId = appointmentId;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        //患者端
        switch (position) {
            case 0: {
                return ReadQuestionsFragment.getInstance(appointmentId, QuestionsPath.NORMAL, true);
            }
            case 1: {
                return ReadDiagnosisFragment.newInstance(appointmentId);
            }
        }
        return null;
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (Settings.isDoctor()) {
            if (position == 0) {
                return "患者问卷";
            } else {
                return "病历记录";
            }
        } else {
            if (position == 0) {
                return "我的问卷";
            } else {
                return "医生建议";
            }
        }
    }
}
