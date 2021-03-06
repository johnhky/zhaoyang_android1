package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.doctor.sun.R;
import com.doctor.sun.RandomUtil;
import com.doctor.sun.TestConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test2EditDoctorInfo {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);


    private void launchActivity() {
        Intent i = new Intent();
        Doctor doctor = new Doctor();
        doctor.setPhone(TestConfig.DOCTOR_PHONE_NUM);
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, EditDoctorInfoFragment.getArgs(doctor));
        mActivityTestRule.launchActivity(i);
    }

    @Test
    public void test2EditDoctorInfo() {
        launchActivity();

        fillDoctorInfo();

        clickNext();
    }


    private void clickNext() {
        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_finish), withText("完成"), withContentDescription("完成"), isDisplayed()));
        actionMenuItemView2.perform(click());
    }

    private void fillDoctorInfo() {

        CustomViewAction.performActionAt(1, typeText("doctor" + RandomUtil.generateString(4)));
        CustomViewAction.performActionAt(5,
                CustomViewAction.clickChildViewWithId(R.id.rb_male));

        CustomViewAction.performActionAt(7, typeText("hospital" + TestConfig.DOCTOR_PHONE_NUM));
        CustomViewAction.performActionAt(9, typeText("specialist" + TestConfig.DOCTOR_PHONE_NUM));

        CustomViewAction.performActionAt(11, typeText(TestConfig.DOCTOR_PHONE_NUM));

        CustomViewAction.performActionAt(13, CustomViewAction.clickChildViewWithId(R.id.btn_title));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(2, click()));
    }

    @After
    public void cleanUp() {
        mActivityTestRule.getActivity().finishAffinity();
    }

}
