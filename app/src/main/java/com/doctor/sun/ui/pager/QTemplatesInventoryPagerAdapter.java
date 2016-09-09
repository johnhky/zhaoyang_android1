package com.doctor.sun.ui.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.doctor.sun.ui.fragment.MyScalesInventoryFragment;
import com.doctor.sun.ui.fragment.QuestionsInventoryFragment;
import com.doctor.sun.ui.fragment.ScalesInventoryFragment;
import com.doctor.sun.util.FragmentFactory;

/**
 * Created by rick on 9/9/2016.
 */

public class QTemplatesInventoryPagerAdapter extends FragmentStatePagerAdapter {

    private final String id;

    public QTemplatesInventoryPagerAdapter(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = null;
        switch (position) {
            case 0: {
                args = MyScalesInventoryFragment.getArgs();
                break;
            }
            case 1: {
                args = ScalesInventoryFragment.getArgs(id);
                break;
            }
            case 2: {
                args = QuestionsInventoryFragment.getArgs(id);
                break;
            }
        }
        if (args != null) {
            return FragmentFactory.getInstance().get(args);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "我的模版";
            }
            case 1: {
                return "量表库";
            }
            case 2: {
                return "问题库";
            }
        }
        return super.getPageTitle(position);
    }
}
