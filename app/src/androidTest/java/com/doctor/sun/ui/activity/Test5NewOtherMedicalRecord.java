package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.RandomUtil;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;

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
public class Test5NewOtherMedicalRecord {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    @Test
    public void test5NewMedicalRecord() {

        Bundle args = NewMedicalRecordFragment.getArgs(1);
        Intent intent = new Intent();
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, args);
        mActivityTestRule.launchActivity(intent);

        CustomViewAction.performActionAt(2,
                typeText("relation" + getTag()));
        CustomViewAction.performActionAt(4,
                typeText("self" + getTag()));
        CustomViewAction.performActionAt(6,
                typeText("patient" + getTag()));
        CustomViewAction.performActionAt(10,
                CustomViewAction.clickChildViewWithId(R.id.rb_male));

        clickNext();
    }

    public String getTag() {
        return RandomUtil.generateString(3);
    }

    private void clickNext() {
        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_confirm), withText("确定"  ), withContentDescription("确定"), isDisplayed()));
        actionMenuItemView.perform(click());
    }

}
