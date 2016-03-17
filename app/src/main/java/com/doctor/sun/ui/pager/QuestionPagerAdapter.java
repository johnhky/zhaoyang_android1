package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.http.Api;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.fragment.ListFragment;
import com.doctor.sun.ui.fragment.QuestionCategoryFragment;
import com.doctor.sun.ui.fragment.QuestionExtendFragment;
import com.doctor.sun.ui.fragment.TemplateExtendFragment;

/**
 * Created by rick on 12/17/15.
 */
public class QuestionPagerAdapter extends FragmentPagerAdapter {
    private String appointmentId;
    private QuestionModule api = Api.of(QuestionModule.class);

    public String getAppointmentId() {
        return appointmentId;
    }

    public QuestionPagerAdapter(FragmentManager fm, String appointmentId) {
        super(fm);
        this.appointmentId = appointmentId;

    }

    @Override
    public Fragment getItem(final int position) {
        ListFragment fragment = null;
        switch (position) {
            case 0: {
                return TemplateExtendFragment.getInstance();
            }
            case 1: {
                return QuestionCategoryFragment.getInstance();
            }
            case 2: {
                return QuestionExtendFragment.getInstance();
            }
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
                return "我的模板";
            case 1:
                return "量表库";
            case 2:
                return "问题库";
            default:
                return "";
        }
    }
}
