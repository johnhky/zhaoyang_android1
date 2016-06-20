package com.doctor.sun.ui.activity;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by rick on 20/6/2016.
 */
public class UMBaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
