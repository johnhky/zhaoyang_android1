package com.doctor.sun.util;

import android.support.v4.app.Fragment;

import com.doctor.sun.ui.fragment.ChangeMyPhoneNumFragment;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.fragment.RegisterFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rick on 17/8/2016.
 */

public class FragmentFactory {
    private final Map<String, FactoryCommand> map = new HashMap<>();

    private static FragmentFactory instance;

    public static FragmentFactory getInstance() {
        if (instance == null) {
            instance = new FragmentFactory();
        }
        return instance;
    }

    public FactoryCommand get(String stringExtra) {
        return map.get(stringExtra);
    }

    public interface FactoryCommand {
        Fragment execute();
    }


    public FragmentFactory() {
        init();
    }

    private void init() {
        map.put(EditDoctorInfoFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new EditDoctorInfoFragment();
            }
        });
        map.put(ChangeMyPhoneNumFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new ChangeMyPhoneNumFragment();
            }
        });
        map.put(RegisterFragment.TAG, new FactoryCommand() {
            @Override
            public Fragment execute() {
                return new RegisterFragment();
            }
        });
    }
}
