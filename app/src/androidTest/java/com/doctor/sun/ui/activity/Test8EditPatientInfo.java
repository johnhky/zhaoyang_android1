package com.doctor.sun.ui.activity;


import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.filters.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.EditPatientInfoFragment;

import org.hamcrest.core.AllOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Test8EditPatientInfo {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    private void launchActivity() {
        Intent i = new Intent();

        Patient patient = new Patient();
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, EditPatientInfoFragment.getArgs(patient));
        mActivityTestRule.launchActivity(i);
    }

    @Test
    public void test8EditPatientInfo() {
        launchActivity();

        enableEdit();
        editInfo();
        saveInfo();
    }

    private void enableEdit() {
        ViewInteraction actionMenuEdit = onView(
                AllOf.allOf(withId(R.id.action_edit), withText("编辑"), isDisplayed()));
        actionMenuEdit.perform(click());

        ViewInteraction actionMenuConfirm = onView(
                AllOf.allOf(withText("确定"), isDisplayed()));
        actionMenuConfirm.perform(click());
    }

    private void editInfo() {
        CustomViewAction.performActionAt(2, typeText("name"));
        CustomViewAction.performActionAt(4, typeText("abc@mail.com"));
        CustomViewAction.performActionAt(8, CustomViewAction.clickChildViewWithId(R.id.rb_female));
    }

    private void saveInfo() {
        ViewInteraction actionMenuSave = onView(
                AllOf.allOf(withId(R.id.action_finish), withText("完成"), isDisplayed()));
        actionMenuSave.perform(click());
    }
}
