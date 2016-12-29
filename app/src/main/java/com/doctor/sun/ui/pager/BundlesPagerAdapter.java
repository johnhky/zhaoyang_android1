package com.doctor.sun.ui.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.util.FragmentFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by rick on 29/12/2016.
 */

public class BundlesPagerAdapter extends FragmentPagerAdapter {
    final private List<Bundle> bundleList;


    public BundlesPagerAdapter(FragmentManager fm, @NotNull List<Bundle> bundleList) {
        super(fm);
        this.bundleList = bundleList;
    }

    @Override
    public int getCount() {
        if (bundleList != null) {
            return bundleList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (0 <= position && position <= bundleList.size()) {
            return FragmentFactory.getInstance().get(bundleList.get(position));
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (0 <= position && position <= bundleList.size()) {
            return bundleList.get(position).getString(Constants.FRAGMENT_TITLE);
        } else {
            return "";
        }
    }
}
