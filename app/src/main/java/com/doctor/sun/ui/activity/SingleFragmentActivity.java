package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.util.FragmentFactory;

/**
 * Created by rick on 17/8/2016.
 */

public class SingleFragmentActivity extends BaseFragmentActivity2 {


    private Fragment instance;

    public static Intent intentFor(Context context, String scalesId, boolean isDone) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_NAME, "EditDoctorInfoFragment");
        i.putExtra(Constants.DATA, scalesId);
        i.putExtra(Constants.READ_ONLY, isDone);
        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_scales_detail);


        instance = FragmentFactory.getInstance().get(getIntent().getStringExtra(Constants.FRAGMENT_NAME)).execute();
        instance.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, instance)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        instance.onActivityResult(requestCode, resultCode, data);
    }
}
