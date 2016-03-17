package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.MyOrderPagerAdapter;

/**
 * Created by rick on 11/1/2016.
 */
public class AppointmentListActivity extends TabActivity {

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AppointmentListActivity.class);
        return i;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new MyOrderPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("我的订单");
        return headerViewModel;
    }
}
