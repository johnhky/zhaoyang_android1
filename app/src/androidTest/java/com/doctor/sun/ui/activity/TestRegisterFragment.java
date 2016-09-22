package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.RegisterFragment;

import org.hamcrest.core.AllOf;
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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestRegisterFragment {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    @Test
    public void testRegisterFragment() {

        Intent i = new Intent();
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, RegisterFragment.getArgs());
        mActivityTestRule.launchActivity(i);

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        CustomViewAction.clickChildViewWithId(R.id.rb_patient)));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, typeText("13922308139")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6, typeText("123456")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6,
                        CustomViewAction.clickChildViewWithId(R.id.btn_captcha)));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(8, typeText("a123456")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(10, typeText("a123456")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(12,
                        CustomViewAction.clickChildViewWithId(R.id.cb_confirm)));

        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_next), withText("下一步"), withContentDescription("下一步"), isDisplayed()));
        actionMenuItemView.perform(click());
    }

}
