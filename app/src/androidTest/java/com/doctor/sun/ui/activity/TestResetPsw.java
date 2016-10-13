package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.TestConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.ResetPswFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestResetPsw {
    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    @Before
    public void setUp() {
        Intent i = new Intent();
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, ResetPswFragment.getArgs());
        mActivityTestRule.launchActivity(i);

    }

    @Test
    public void resetPswActivityTest() {


         CustomViewAction.performActionAt(0, typeText(TestConfig.PHONE_NUM_TO_CHANGE_PSW));
         CustomViewAction.performActionAt(2, typeText(TestConfig.CAPTCHA));
         CustomViewAction.performActionAt(4, typeText(TestConfig.CHANGED_PSW));
         CustomViewAction.performActionAt(6, typeText(TestConfig.CHANGED_PSW));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_next), withText("下一步"), withContentDescription("下一步"), isDisplayed()));
        actionMenuItemView.perform(click());

    }

}
