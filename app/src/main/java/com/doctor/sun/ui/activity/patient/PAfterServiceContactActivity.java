package com.doctor.sun.ui.activity.patient;

import android.support.v4.view.PagerAdapter;

import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.DoctorRelationshipListPA;

/**
 * Created by rick on 29/8/2016.
 */

public class PAfterServiceContactActivity extends TabActivity {
    @Override
    protected PagerAdapter createPagerAdapter() {
        return new DoctorRelationshipListPA(getSupportFragmentManager());
    }
}
