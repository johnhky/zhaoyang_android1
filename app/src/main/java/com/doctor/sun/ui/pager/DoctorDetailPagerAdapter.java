package com.doctor.sun.ui.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.fragment.DoctorArticleFragment;
import com.doctor.sun.ui.fragment.DoctorCommentFragment;
import com.doctor.sun.ui.fragment.DoctorDescriptionFragment;

/**
 * Created by rick on 1/4/2016.
 */
public class DoctorDetailPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"医生简介", "文章", "评论"};
    private Doctor doctor;


    public DoctorDetailPagerAdapter(FragmentManager fm, Doctor doctor) {
        super(fm);
        this.doctor = doctor;
    }

    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case 0:
                return DoctorDescriptionFragment.getInstance(doctor.getDetail());
            case 1:
                return DoctorArticleFragment.getInstance(doctor.getId());
            case 2:
                return DoctorCommentFragment.getInstance(doctor.getId());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = titles[position];
        return title;
    }

}
