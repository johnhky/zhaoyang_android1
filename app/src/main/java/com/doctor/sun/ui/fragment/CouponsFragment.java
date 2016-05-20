package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 20/5/2016.
 */
public class CouponsFragment extends ListFragment {
    private ProfileModule api = Api.of(ProfileModule.class);

    public static CouponsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(Constants.TYPE, type);

        CouponsFragment fragment = new CouponsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadMore() {
        api.coupons(getArguments().getString(Constants.TYPE)).enqueue(getCallback());
    }
}
