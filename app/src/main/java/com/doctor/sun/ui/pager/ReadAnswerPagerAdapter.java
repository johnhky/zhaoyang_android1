package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.ui.fragment.WaitingSuggestionFragment;

/**
 * Created by rick on 1/8/2016.
 */

public class ReadAnswerPagerAdapter extends FragmentStatePagerAdapter {

    String recordId;
    String appointmentId;
    int status;
    int canEdit;
    String isDiagnosis;

    public ReadAnswerPagerAdapter(FragmentManager fm, Appointment appointment) {
        super(fm);
        recordId = String.valueOf(appointment.getRecord_id());
        appointmentId = String.valueOf(appointment.getId());
        status = appointment.getStatus();
        canEdit = appointment.getCan_edit();
        isDiagnosis = appointment.getDiagnosis_record()+"";
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
                    if (isAppointmentFinished()) {
                        return ReadDiagnosisFragment.newInstance(appointmentId);
                    } else {
                        //等待医生建议
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
//                    if (isAppointmentFinished() && canEdit == IntBoolean.FALSE) {
//                        return ReadDiagnosisFragment.newInstance(appointmentId);
//                    }
                    if (isAppointmentFinished()) {
                        return ReadDiagnosisFragment.newInstance(appointmentId);
                    } else {
                        if (isDiagnosis.equals("0")) {
                            return DiagnosisFragment.newInstance(appointmentId, recordId);
                        } else {
                            return ReadDiagnosisFragment.newInstance(appointmentId);
                        }
                    }
                }
            }
        }
        return null;
    }


    public boolean isAppointmentWaitting() {
        return AppointmentHandler2.Status.WAITING == status;
    }

    public boolean isAppointmentFinished() {
        return AppointmentHandler2.Status.FINISHED == status;
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
