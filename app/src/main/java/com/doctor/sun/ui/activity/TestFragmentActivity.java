package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.util.FragmentFactory;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 17/8/2016.
 * TODO: 打包的时候注释掉
 */

public class TestFragmentActivity extends BaseFragmentActivity2 {

    public static Intent intentFor(Context context, String title, Bundle bundle) {
        Intent intent = new Intent(context, TestFragmentActivity.class);
        intent.putExtra(Constants.FRAGMENT_TITLE, title);
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, bundle);
        return intent;
    }


    private Fragment instance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_fragment_wraper);


        Bundle bundleExtra = getIntent().getBundleExtra(Constants.FRAGMENT_CONTENT_BUNDLE);
        instance = FragmentFactory.getInstance().get(bundleExtra);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, instance)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EventHub.post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public String getMidTitleString() {
        return getStringExtra(Constants.FRAGMENT_TITLE);
    }

}
