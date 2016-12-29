package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.pager.BundlesPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 29/12/2016.
 */

public class BundlesTabActivity extends TabActivity {

    public static Intent intentFor(Context context, Bundle... bundles) {
        Intent intent = new Intent(context, BundlesTabActivity.class);
        if (bundles != null) {
            for (int i = 0; i < bundles.length; i++) {
                intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE + i, bundles[i]);
            }
        }
        return intent;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new BundlesPagerAdapter(getSupportFragmentManager(), getBundles());
    }

    public List<Bundle> getBundles() {
        List<Bundle> bundles = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Bundle bundleExtra = getIntent().getBundleExtra(Constants.FRAGMENT_CONTENT_BUNDLE + i);
            if (bundleExtra == null) {
                break;
            } else {
                bundles.add(bundleExtra);
            }
        }
        return bundles;
    }
}
