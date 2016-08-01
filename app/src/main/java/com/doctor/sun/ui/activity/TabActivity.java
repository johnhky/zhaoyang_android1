package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityTabTwoBinding;
import com.doctor.sun.ui.fragment.BaseFragment;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 12/18/15.
 */
public abstract class TabActivity extends BaseFragmentActivity2 implements HeaderViewModel.HeaderView {

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

        initHeader();
    }

    private void setCurrentItem() {
        int position = getPosition();
        if (position <= pagerAdapter.getCount()) {
            binding.vp.setCurrentItem(position);
        }
    }

    private void initHeader() {
        BaseFragment activeFragment = (BaseFragment) getActiveFragment(binding.vp, getPosition());
        if (activeFragment != null && activeFragment.getHeader() != null) {
            binding.setHeader(activeFragment.getHeader());
        } else {
            HeaderViewModel header = createHeaderViewModel();
            binding.setHeader(header);
        }
    }

    private void initPagerAdapter() {
        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);
    }

    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment activeFragment = (BaseFragment) getActiveFragment(binding.vp, position);
                if (activeFragment != null && activeFragment.getHeader() != null) {
                    binding.setHeader(activeFragment.getHeader());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return getSupportFragmentManager().findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    protected ActivityTabTwoBinding getBinding() {
        return binding;
    }

    protected abstract PagerAdapter createPagerAdapter();

    protected abstract HeaderViewModel createHeaderViewModel();

}
