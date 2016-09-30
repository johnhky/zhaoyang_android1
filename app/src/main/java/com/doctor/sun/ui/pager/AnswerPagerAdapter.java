package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;

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
                    if (isAppointmentFinished()) {
                        return ReadQuestionsFragment.getInstance(appointment.getIdString(), QuestionsPath.NORMAL, true);
                    } else {
                        return AnswerQuestionFragment.getInstance(appointment.getIdString(), QuestionsPath.NORMAL);
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
                    if (isAppointmentFinished()) {
                        return ReadQuestionsFragment.getInstance(appointment.getIdString(), QuestionsPath.NORMAL, true);
                    } else {
                        return ReadQuestionsFragment.getInstance(appointment.getIdString(), QuestionsPath.NORMAL, false);
                    }
                }
                case 1: {
//                appointment.setId(325);
                    if (!isAppointmentFinished()) {
                        return DiagnosisFragment.newInstance(appointment);
                    } else {
                        return ReadDiagnosisFragment.newInstance(appointment.getId());
                    }
                }
            }
        }
        return null;
    }

    public boolean isAppointmentFinished() {
        return AppointmentHandler.Status.A_FINISHED.equals(appointment.getDisplayStatus());
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
