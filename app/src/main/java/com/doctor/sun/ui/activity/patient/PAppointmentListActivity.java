package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.widget.LinearLayout;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.ui.activity.TabActivity;

import com.doctor.sun.ui.pager.MyOrderPagerAdapter;
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.Config;

/**
 * Created by rick on 11/1/2016.
 */
public class PAppointmentListActivity extends TabActivity {

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PAppointmentListActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBinding().vp.setOffscreenPageLimit(3);
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
