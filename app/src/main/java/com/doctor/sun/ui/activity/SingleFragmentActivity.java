package com.doctor.sun.ui.activity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_fragment_wraper);


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

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.FRAGMENT_TITLE);
    }
}
