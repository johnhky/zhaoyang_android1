package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.TestConfig;
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
public class Test3RegisterPatient {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, true, false);


    private void launchActivity() {
        Intent i = new Intent();
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, RegisterFragment.getArgs());
        mActivityTestRule.launchActivity(i);
    }

    @Test
    public void test3RegisterPatient() {
        launchActivity();
        int REGISTER_TYPE = R.id.rb_patient;
        selectRegisterType(REGISTER_TYPE);

        typePhoneNum(TestConfig.PATIENT_PHONE_NUM);

        fillRegisterInfo();

        clickNext();
    }

    private void typePhoneNum(String doctorPhoneNum) {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, typeText(doctorPhoneNum)));
    }

    private void selectRegisterType(int id) {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        CustomViewAction.clickChildViewWithId(id)));

    }

    private void clickNext() {
        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_next), withText("下一步"), withContentDescription("下一步"), isDisplayed()));
        actionMenuItemView.perform(click());
    }

    private void fillRegisterInfo() {

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6, typeText(TestConfig.CAPTCHA)));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(6,
                        CustomViewAction.clickChildViewWithId(R.id.btn_captcha)));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(8, typeText(TestConfig.PSW)));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(10, typeText(TestConfig.PSW)));
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(12,
                        CustomViewAction.clickChildViewWithId(R.id.cb_confirm)));
    }
}
