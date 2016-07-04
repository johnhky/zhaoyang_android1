package com.doctor.sun.ui.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.doctor.sun.BuildConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by rick on 20/6/2016.
 */
public class BaseFragment extends Fragment {
    public final String TAG = getClass().getSimpleName();


    public void onResume() {
        super.onResume();
        if (BuildConfig.DEV_MODE) {
            Log.e(TAG, "onResume: " + TAG);
        }
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

}
