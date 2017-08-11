package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.fragment.PAppointmentListFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;
import com.doctor.sun.ui.fragment.SearchTabActivity;
import com.doctor.sun.ui.pager.MyOrderPagerAdapter2;

import java.util.ArrayList;

/**
 * Created by kb on 22/12/2016.
 */

public class MyOrderActivity extends SearchTabActivity {

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MyOrderActivity.class);
        return i;
    }

    public static Intent makeIntent(Context context, int position) {
        Intent i = new Intent(context, MyOrderActivity.class);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBinding().vp.setOffscreenPageLimit(3);
        getBinding().vp.setCurrentItem(getIntent().getIntExtra(Constants.POSITION, 0));
        binding.llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.llSearch.setVisibility(View.GONE);
                binding.llSearchBody.setVisibility(View.VISIBLE);
            }
        });
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initPagerAdapter(1,s.toString());
            }
        });

    }

    @Override
    protected void initPagerTabs() {
        binding.pagerTabs.setCustomTabView(R.layout.tab_with_unread_count, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }

    @Override
    protected PagerAdapter createPagerAdapter(int position,String text) {
        if (position == 1) {
            return new MyPagerAdapter(getSupportFragmentManager(),text);
        }
        return new MyOrderPagerAdapter2(getSupportFragmentManager());
    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {

        String text;

        public MyPagerAdapter(FragmentManager fragmentManager, String text) {
            super(fragmentManager);
            this.text = text;
        }

        @Override
        public Fragment getItem(int position) {
            return PAppointmentListFragment.newInstance(text);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

}
