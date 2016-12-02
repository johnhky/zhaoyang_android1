package com.doctor.sun.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.fragment.BaseFragment;
import com.doctor.sun.ui.fragment.BaseFragmentFactory;

/**
 * Created by rick on 17/8/2016.
 */

public class FragmentFactory {
    private static FragmentFactory instance;
    private BaseFragmentFactory factory = new BaseFragmentFactory();

    public static FragmentFactory getInstance() {
        if (instance == null) {
            instance = new FragmentFactory();
        }
        return instance;
    }

    private Fragment get(String name) {
        return factory.create(name);
    }

    public Fragment get(Bundle args) {
        String name = args.getString(Constants.FRAGMENT_NAME);
        BaseFragment baseFragment = factory.create(name);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    // private constructor
    private FragmentFactory() {
    }
}
