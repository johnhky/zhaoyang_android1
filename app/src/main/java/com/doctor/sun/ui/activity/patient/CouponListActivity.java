package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.http.Api;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.ListActivity2;

/**
 * Created by rick on 20/5/2016.
 */
public class CouponListActivity extends ListActivity2<Coupon> {
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, CouponListActivity.class);
        return intent;
    }

    @Override
    protected void loadMore() {
        api.coupons("all").enqueue(getCallback());
    }
}
