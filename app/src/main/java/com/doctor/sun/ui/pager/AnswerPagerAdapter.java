package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionFragment;

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
        if (!Settings.isDoctor()) {
            switch (position) {
                case 0: {
                    //填写问卷 编辑
                    if (AppointmentHandler.Status.A_FINISHED.equals(appointment.getDisplayStatus())) {
                        return ReadQuestionFragment.getInstance(appointment.getId());
                    } else {
                        return AnswerQuestionFragment.getInstance(appointment.getId());
                    }
                }
                case 1: {
//                appointment.setId(325);
                    return ReadDiagnosisFragment.newInstance(appointment.getId());
                }
            }
        } else {
            switch (position) {
                case 0: {
                    //填写问卷 只读
                    return ReadQuestionFragment.getInstance(appointment.getId());
                }
                case 1: {
//                appointment.setId(325);
                    if (AppointmentHandler.Status.A_FINISHED.equals(appointment.getDisplayStatus())) {
                        return DiagnosisFragment.newInstance(appointment);
                    } else {
                        return ReadDiagnosisFragment.newInstance(appointment.getId());
                    }
                }
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
        if (position == 0) {
            return "我的问卷";
        } else {
            return "医生建议";
        }
    }
}
