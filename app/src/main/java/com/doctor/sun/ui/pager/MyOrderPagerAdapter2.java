package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.Settings;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.ui.fragment.PAfterServiceFragment;
import com.doctor.sun.ui.fragment.PApplyingDoctorListFragment;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;

/**
 * Created by kb on 22/12/2016.
 */

public class MyOrderPagerAdapter2 extends FragmentStatePagerAdapter {

    private PatientDTO patientDTO;

    public MyOrderPagerAdapter2(FragmentManager fm) {
        super(fm);
        patientDTO = Settings.getPatientDTO();
    }

    @Override
    public Fragment getItem(int position) {
        RefreshListFragment fragment = null;
        switch (position) {
            case 0:
                fragment = PAppointmentListFragment.newInstance(1);
                break;
            case 1:
                fragment = PAfterServiceFragment.newInstance();
                break;
            case 2:
                fragment = PApplyingDoctorListFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "咨询订单," + patientDTO.appointmentNum;
            case 1:
                return "随访订单," + patientDTO.followUpDoingNum;
            case 2:
                return  "随访关系," + patientDTO.applyingNum;
        }
        return "";
    }
}
