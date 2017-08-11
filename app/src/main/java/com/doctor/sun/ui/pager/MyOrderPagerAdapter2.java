package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.ui.fragment.PAppointmentDoingFragment;
import com.doctor.sun.ui.fragment.PAppointmentDoneFragment;
import com.doctor.sun.ui.fragment.PAppointmentInvalidFragment;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.PAppointmentWaittingFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

import java.util.ArrayList;

/**
 * Created by kb on 22/12/2016.
 */

public class MyOrderPagerAdapter2 extends FragmentStatePagerAdapter {

    private PatientDTO patientDTO;
    private ArrayList<String> status;

    public MyOrderPagerAdapter2(FragmentManager fm) {
        super(fm);
        patientDTO = Settings.getPatientDTO();
    }

    @Override
    public Fragment getItem(int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0:
                fragment = PAppointmentListFragment.newInstance("");
                break;
            case 1:
                status = new ArrayList<>();
                status.add("1");
                fragment = PAppointmentWaittingFragment.newInstance(status);
                break;
            case 2:
                status = new ArrayList<>();
                status.add("2");
                status.add("3");
                fragment = PAppointmentDoingFragment.newInstance(status);
                break;
            case 3:
                status = new ArrayList<>();
                status.add("4");
                fragment = PAppointmentDoneFragment.newInstance(status);
                break;
            case 4:
                status = new ArrayList<>();
                status.add("5");
                status.add("6");
                status.add("7");
                status.add("8");
                fragment = PAppointmentInvalidFragment.newInstance(status);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "全部," + (patientDTO == null ? "" : 0);
            case 1:
                return "候诊中," + (patientDTO == null ? "" : patientDTO.getAppointment_status_num().getWaitting());
            case 2:
                return "就诊中," + (patientDTO == null ? "" : patientDTO.getAppointment_status_num().getDoing() + patientDTO.getAppointment_status_num().getWaittingSuggest());
            case 3:
                return "已完成," + (patientDTO == null ? "" : patientDTO.applyingNum);
            case 4:
                return "失效单," + (patientDTO == null ? "" : patientDTO.applyingNum);
        }
        return "";
    }
}
