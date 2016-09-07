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

public class LeftDrawerFragmentActivity extends BaseFragmentActivity2 {

    public static Intent intentFor(Context context, String title, Bundle contentArgs, Bundle drawerArgs) {
        Intent intent = new Intent(context, LeftDrawerFragmentActivity.class);
        intent.putExtra(Constants.FRAGMENT_TITLE, title);
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, contentArgs);
        intent.putExtra(Constants.FRAGMENT_LEFT_DRAWER_BUNDLE, drawerArgs);
        return intent;
    }

    private Fragment instance;
    private Fragment leftDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_left_drawer_wraper);


        Bundle contentArgs = getIntent().getBundleExtra(Constants.FRAGMENT_CONTENT_BUNDLE);
        instance = FragmentFactory.getInstance().get(contentArgs.getString(Constants.FRAGMENT_NAME)).execute();
        instance.setArguments(contentArgs);

        Bundle leftDrawerArgs = getIntent().getBundleExtra(Constants.FRAGMENT_LEFT_DRAWER_BUNDLE);
        leftDrawer = FragmentFactory.getInstance().get(leftDrawerArgs.getString(Constants.FRAGMENT_NAME)).execute();
        leftDrawer.setArguments(leftDrawerArgs);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, instance)
                .replace(R.id.fly_left_drawer, leftDrawer)
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
