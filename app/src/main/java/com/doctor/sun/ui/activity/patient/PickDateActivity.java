package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPickDateBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import com.doctor.sun.ui.pager.PickDatePagerAdapter;

/**
 * Created by rick on 7/1/2016.
 */
public class PickDateActivity extends BaseFragmentActivity2 {

    protected ActivityPickDateBinding binding;
    private PagerAdapter pagerAdapter;

    private AppointmentBuilder getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_date);


        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);

        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);

        binding.setData(getData());
    }

    protected PagerAdapter createPagerAdapter() {
        AppointmentBuilder data = getData();
        return new PickDatePagerAdapter(getSupportFragmentManager(), data);
    }



    public static Intent makeIntent(Context context, AppointmentBuilder builder) {
        Intent i = new Intent(context, PickDateActivity.class);
        i.putExtra(Constants.DATA, builder);
        return i;
    }


    @Override
    public int getMidTitle() {
        return R.string.title_pick_date;
    }
}
