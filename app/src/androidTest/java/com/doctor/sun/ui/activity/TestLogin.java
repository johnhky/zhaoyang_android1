package com.doctor.sun.ui.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.TestConfig;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestLogin {

    public static final String LOGIN_PHONE_NUM = TestConfig.getPatientPhoneNum();
    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void test20loginDoctor() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_phone), isDisplayed()));
        appCompatEditText.perform(replaceText(TestConfig.DOCTOR_PHONE_NUM), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_password), isDisplayed()));
        appCompatEditText2.perform(replaceText(TestConfig.PSW), closeSoftKeyboard());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("登    录"), isDisplayed()));
        appCompatTextView.perform(click());

    }

    @Test
    public void test40loginPatient() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_phone), isDisplayed()));
        appCompatEditText.perform(replaceText(TestConfig.getPatientPhoneNum()), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_password), isDisplayed()));
        appCompatEditText2.perform(replaceText(TestConfig.PSW), closeSoftKeyboard());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("登    录"), isDisplayed()));
        appCompatTextView.perform(click());

    }
}
