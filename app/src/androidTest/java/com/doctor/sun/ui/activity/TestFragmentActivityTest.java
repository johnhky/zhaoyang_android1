package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.fragment.RegisterFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestFragmentActivityTest {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);


    @Test
    public void testFragmentActivityTest() {

        Bundle args = RegisterFragment.getArgs();
        Intent i = new Intent();
        i.putExtra(Constants.FRAGMENT_TITLE, "测试");
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, args);
        mActivityTestRule.launchActivity(i);


    }
}
