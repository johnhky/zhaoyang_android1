package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.fragment.QuestionCustomFragment;
import com.doctor.sun.ui.fragment.QuestionDefaultFragment;

/**
 * Created by lucas on 12/24/15.
 */
public class QuestionBankPagerAdapter extends FragmentPagerAdapter {

    public QuestionBankPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return QuestionDefaultFragment.getInstance();
            case 1:
                return QuestionCustomFragment.getInstance();
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
            return "昭阳题库";
        } else {
            return "自编题";
        }
    }
}
