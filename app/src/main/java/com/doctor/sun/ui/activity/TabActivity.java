package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityTabTwoBinding;


/**
 * Created by rick on 12/18/15.
 */
public abstract class TabActivity extends BaseFragmentActivity2 {

    protected ActivityTabTwoBinding binding;
    private PagerAdapter pagerAdapter;

    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tab_two);


        initPagerAdapter();

        initPagerTabs();

        setCurrentItem();

    }

    private void setCurrentItem() {
        int position = getPosition();
        if (position >= 0 && position <= pagerAdapter.getCount()) {
            binding.vp.setCurrentItem(position);
        }
    }


    protected void initPagerAdapter() {
        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);
    }

    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }

    protected Fragment getActiveFragment(ViewPager container, int position) {
        String name = getFragmentTag(container.getId(), position);
        return getSupportFragmentManager().findFragmentByTag(name);
    }

    private static String getFragmentTag(int containerId, int adapterPosition) {
        return "android:switcher:" + containerId + ":" + adapterPosition;
    }

    protected ActivityTabTwoBinding getBinding() {
        return binding;
    }

    protected abstract PagerAdapter createPagerAdapter();


}
