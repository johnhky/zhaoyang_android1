package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
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
public class ResetPswFragmentTest {

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


        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, typeText("13922304745")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, typeText("123456")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, typeText("a123456")));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6, typeText("a123456")));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_next), withText("下一步"), withContentDescription("下一步"), isDisplayed()));
        actionMenuItemView.perform(click());

    }

}
