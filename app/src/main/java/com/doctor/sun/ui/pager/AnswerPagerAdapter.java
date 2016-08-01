package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;

/**
 * Created by rick on 1/8/2016.
 */

public class AnswerPagerAdapter extends FragmentPagerAdapter {


    private final Appointment appointment;

    public AnswerPagerAdapter(FragmentManager fm, Appointment appointmentId) {
        super(fm);
        this.appointment = appointmentId;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AnswerQuestionFragment.getInstance(appointment.getId());
        } else {
            return ReadDiagnosisFragment.newInstance(appointment.getId());
        }
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
        if (position == 0) {
            return "我的问卷";
        } else {
            return "医生建议";
        }
    }
}
