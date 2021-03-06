package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.pager.PatientRelationshipListPA;

/**
 * Created by rick on 1/6/2016.
 */
public class PAfterServiceActivity extends TabActivity {

    public static Intent intentFor(Context context) {
        return new Intent(context, PAfterServiceActivity.class);
    }

    public static Intent intentFor(Context context, int position) {
        Intent intent = new Intent(context, PAfterServiceActivity.class);
        intent.putExtra(Constants.POSITION, position);

        return intent;
    }

    @Override
    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_with_unread_count, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }

    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(this, ContactActivity.PATIENTS_CONTACT);
        startActivity(intent);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_p_follow_up;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new PatientRelationshipListPA(getSupportFragmentManager());
    }
}
