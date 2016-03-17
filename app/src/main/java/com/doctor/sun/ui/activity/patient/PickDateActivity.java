package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPickDateBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.PickDatePagerAdapter;

/**
 * Created by rick on 7/1/2016.
 */
public class PickDateActivity extends BaseFragmentActivity2 {

    protected ActivityPickDateBinding binding;
    private PagerAdapter pagerAdapter;


    public static Intent makeIntent(Context context, Doctor data, int type) {
        Intent i = new Intent(context, PickDateActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, type);
        return i;
    }

    public int getType() {
        return getIntent().getIntExtra(Constants.TYPE, -1);
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_date);

        HeaderViewModel header = createHeaderViewModel();
        binding.setHeader(header);

        pagerAdapter = createPagerAdapter();
        binding.vp.setAdapter(pagerAdapter);

        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);

        binding.setData(getData());
    }

    protected PagerAdapter createPagerAdapter() {
        Doctor data = getData();
        return new PickDatePagerAdapter(getSupportFragmentManager(), data, getType());
    }

    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("选择日期");
        return headerViewModel;
    }

}
