package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.ui.fragment.WaitingSuggestionFragment;

/**
 * Created by rick on 1/8/2016.
 */

public class AnswerPagerAdapter extends FragmentStatePagerAdapter {

    String recordId;
    String appointmentId;
    String displayStatus;
    int canEdit;


    public AnswerPagerAdapter(FragmentManager fm, Appointment appointment) {
        super(fm);
        recordId = String.valueOf(appointment.getRecordId());
        appointmentId = String.valueOf(appointment.getId());
        displayStatus = appointment.getDisplayStatus();
        canEdit = appointment.canEdit;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        //患者端
        if (!Settings.isDoctor()) {
            switch (position) {
                case 0: {
                    //填写问卷 编辑
                    if (isAppointmentFinished()) {
                        return ReadQuestionsFragment.getInstance(appointmentId, QuestionsPath.NORMAL, true);
                    } else {
                        return AnswerQuestionFragment.getInstance(appointmentId, QuestionsPath.NORMAL);
                    }
                }
                case 1: {
//                appointment.setId(325);
                    if (canEdit != IntBoolean.FALSE) {
                        return ReadDiagnosisFragment.newInstance(appointmentId);
                    } else {
                        return WaitingSuggestionFragment.newInstance();
                    }
                }
            }
        } else {
            switch (position) {
                case 0: {
                    //填写问卷 只读
                    if (isAppointmentFinished()) {
                        return ReadQuestionsFragment.getInstance(appointmentId, QuestionsPath.NORMAL, true);
                    } else {
                        return ReadQuestionsFragment.getInstance(appointmentId, QuestionsPath.NORMAL, false);
                    }
                }
                case 1: {
//                appointment.setId(325);
                    if (canEdit != IntBoolean.FALSE) {
                        return DiagnosisFragment.newInstance(appointmentId, recordId);
                    } else {
                        return ReadDiagnosisFragment.newInstance(appointmentId);
                    }
                }
            }
        }
        return null;
    }

    public boolean isAppointmentFinished() {
        return AppointmentHandler.Status.FINISHED.equals(displayStatus);
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
