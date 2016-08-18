package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.QTemplateType;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadDiagnosisFragment;
import com.doctor.sun.ui.fragment.ReadQuestionFragment;

import io.ganguo.library.Config;

/**
 * Created by rick on 12/17/15.
 */
public class ConsultingDetailPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = ConsultingDetailPagerAdapter.class.getSimpleName();
    private Appointment appointment;
    private boolean isReadOnly = false;

    public ConsultingDetailPagerAdapter(FragmentManager fm, Appointment appointment, boolean isReadOnly) {
        super(fm);
        this.appointment = appointment;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public Fragment getItem(final int position) {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            switch (position) {
                case 0: {
                    //填写问卷 编辑
                    return AnswerQuestionFragment.getInstance(appointment.getIdString(), QTemplateType.NORMAL);
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
                    return ReadQuestionFragment.getInstance(appointment.getIdString(), QTemplateType.NORMAL,true);
                }
                case 1: {
//                appointment.setId(325);
                    if (!isReadOnly) {
                        return DiagnosisFragment.newInstance(appointment);
                    } else {
                        return ReadDiagnosisFragment.newInstance(appointment.getId());
                    }
                }
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
        if (position == 0) {
            if (Settings.isDoctor()) {
                return "患者问卷";
            } else {
                return "我的问卷";
            }
        } else {
            if (Settings.isDoctor()) {
                return "病历记录";
            } else {
                return "医生建议";
            }
        }
    }
}
