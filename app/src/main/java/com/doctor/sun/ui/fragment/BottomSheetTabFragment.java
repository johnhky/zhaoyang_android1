package com.doctor.sun.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentTabBinding;
import com.doctor.sun.ui.widget.vpbs.ViewPagerBottomSheetDialogFragment;


/**
 * Created by rick on 12/18/15.
 */
public abstract class BottomSheetTabFragment extends ViewPagerBottomSheetDialogFragment {

    private FragmentTabBinding binding;
    private PagerAdapter pagerAdapter;

    public int getPosition() {
        return getArguments().getInt(Constants.POSITION, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.fragment_tab, container, true);

        return binding.getRoot();
    }

    protected void setCurrentItem() {
        int position = getPosition();
        if (position <= pagerAdapter.getCount()) {
            binding.bottomSheetViewpager.setCurrentItem(position);
            binding.tbTitle.setText(getTitle());
        }
    }

    protected String getTitle() {
        return "";
    }


    protected void initPagerAdapter() {
        pagerAdapter = createPagerAdapter();
        binding.bottomSheetViewpager.setAdapter(pagerAdapter);
    }

    protected void setPagerAdapter(PagerAdapter adapter) {
        binding.bottomSheetViewpager.setAdapter(adapter);
    }

    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.bottomSheetViewpager);
    }

    protected FragmentTabBinding getBinding() {
        return binding;
    }

    protected abstract PagerAdapter createPagerAdapter();


}
