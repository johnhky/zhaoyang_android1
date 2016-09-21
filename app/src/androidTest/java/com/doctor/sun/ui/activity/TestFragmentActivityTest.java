package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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

//        ViewInteraction actionMenuItemView = onView(
//                allOf(withId(com.doctor.sun.R.id.action_save), withText("保存"), withContentDescription("保存"), isDisplayed()));
//        actionMenuItemView.perform(click());
//
//        ViewInteraction textView = onView(
//                allOf(withId(com.doctor.sun.R.id.content), withText("是否结束本次随访"),
//                        childAtPosition(
//                                allOf(withId(com.doctor.sun.R.id.contentScrollView),
//                                        childAtPosition(
//                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
//                                                1)),
//                                0),
//                        isDisplayed()));
//        textView.check(matches(withText("是否结束本次随访")));
//
//        ViewInteraction textView2 = onView(
//                allOf(withId(com.doctor.sun.R.id.buttonDefaultPositive), withText("保存并结束"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                3),
//                        isDisplayed()));
//        textView2.check(matches(withText("保存并结束")));
//
//        ViewInteraction mDButton = onView(
//                allOf(withId(com.doctor.sun.R.id.buttonDefaultPositive), withText("保存并结束"), isDisplayed()));
//        mDButton.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
