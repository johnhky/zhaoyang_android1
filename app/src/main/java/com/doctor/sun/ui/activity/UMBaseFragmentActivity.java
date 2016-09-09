package com.doctor.sun.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by rick on 20/6/2016.
 */
public class UMBaseFragmentActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getLocalClassName());
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getLocalClassName());
        MobclickAgent.onPause(this);
    }

}
