package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.MyOrderPagerAdapter;

/**
 * Created by rick on 11/1/2016.
 */
public class PAppointmentListActivity extends TabActivity {

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PAppointmentListActivity.class);
        return i;
    }

    public static Intent makeIntent(Context context, int position) {
        Intent i = new Intent(context, PAppointmentListActivity.class);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBinding().vp.setOffscreenPageLimit(3);
        getBinding().vp.setCurrentItem(getIntent().getIntExtra(Constants.POSITION, 0));
    }

    @Override
    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_with_unread_count, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new MyOrderPagerAdapter(getSupportFragmentManager());
    }


    @Override
    public int getMidTitle() {
        return R.string.title_my_order;
    }
}
