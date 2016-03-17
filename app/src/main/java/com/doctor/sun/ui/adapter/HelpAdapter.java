package com.doctor.sun.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.ui.fragment.HelpFragment;

/**
 * Created by lucas on 2/2/16.
 */
public class HelpAdapter extends FragmentPagerAdapter {
    private int type;

    public HelpAdapter(FragmentManager fm, int type) {
        super(fm);
        this.type = type;
        Log.e("TAG", "HelpAdapter: " + this.type);
    }

    @Override
    public int getCount() {
        return 6;
    }


    @Override
    public Fragment getItem(int position) {
        if (type == 1) {
            switch (position) {
                case 0:
                    return HelpFragment.newInstance(R.drawable.bg_patient_help1);
                case 1:
                    return HelpFragment.newInstance(R.drawable.bg_patient_help2);
                case 2:
                    return HelpFragment.newInstance(R.drawable.bg_patient_help3);
                case 3:
                    return HelpFragment.newInstance(R.drawable.bg_patient_help4);
                case 4:
                    return HelpFragment.newInstance(R.drawable.bg_patient_help5);
                case 5:
                    return HelpFragment.newInstance(R.drawable.bg_default);
            }
        } else {
            switch (position) {
                case 0:
                    return HelpFragment.newInstance(R.drawable.bg_doctor_help1);
                case 1:
                    return HelpFragment.newInstance(R.drawable.bg_doctor_help2);
                case 2:
                    return HelpFragment.newInstance(R.drawable.bg_doctor_help3);
                case 3:
                    return HelpFragment.newInstance(R.drawable.bg_doctor_help4);
                case 4:
                    return HelpFragment.newInstance(R.drawable.bg_doctor_help5);
                case 5:
                    return HelpFragment.newInstance(R.drawable.bg_default);
            }
        }
        return null;
    }
}
