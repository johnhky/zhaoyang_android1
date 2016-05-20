package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.CouponPagerAdapter;

/**
 * Created by rick on 20/5/2016.
 */
public class CouponTabActivity extends TabActivity {

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, CouponTabActivity.class);
        return intent;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new CouponPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("优惠券");
        return headerViewModel;
    }
}
