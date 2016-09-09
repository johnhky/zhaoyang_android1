package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.QTemplatesInventoryPagerAdapter;

/**
 * Created by rick on 9/9/2016.
 */

public class TemplatesInventoryActivity extends TabActivity {

    public static Intent intentFor(Context context, String appointmentId) {
        Intent intent = new Intent(context, TemplatesInventoryActivity.class);
        intent.putExtra(Constants.DATA, appointmentId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.vp.setOffscreenPageLimit(3);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new QTemplatesInventoryPagerAdapter(getSupportFragmentManager(), getStringExtra(Constants.DATA));
    }
}
