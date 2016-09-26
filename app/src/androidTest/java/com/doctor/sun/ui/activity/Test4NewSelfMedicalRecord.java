package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test4NewSelfMedicalRecord {
    String chars = "abcdefghijklmnopqrstuvwxyz";

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    @Test
    public void test4NewSelfMedicalRecord() {

        Bundle args = NewMedicalRecordFragment.getArgs(0);
        Intent intent = new Intent();
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, args);
        mActivityTestRule.launchActivity(intent);

         CustomViewAction.performActionAt(2,
                        typeText("name" + getTag()));
         CustomViewAction.performActionAt(4,
                        typeText("email" + getTag() + "@qq.com"));
         CustomViewAction.performActionAt(8,
                        CustomViewAction.clickChildViewWithId(R.id.rb_male));

        clickNext();
    }

    public String getTag() {
        return generateString(chars, 6);
    }

    private String generateString(String chars, int length) {
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = chars.charAt(rng.nextInt(chars.length()));
        }
        return new String(text);
    }

    private void clickNext() {
        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_confirm), withText("确定"), withContentDescription("确定"), isDisplayed()));

        actionMenuItemView.perform(click());
    }
}
