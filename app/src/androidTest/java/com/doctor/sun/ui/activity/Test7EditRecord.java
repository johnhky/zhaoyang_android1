package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.EditRecordFragment;

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
public class Test7EditRecord {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    private void launchActivity() {
        Intent i = new Intent();

        MedicalRecord medicalRecord = new MedicalRecord();
        i.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, EditRecordFragment.getArgs(medicalRecord));
        mActivityTestRule.launchActivity(i);
    }

    @Test
    public void test7EditRecord() {
        launchActivity();

        enableEdit();
        editRecord();
        saveRecord();
    }

    private void enableEdit() {
        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_edit), withText("编辑"), isDisplayed()));
        actionMenuItemView.perform(click());
    }

    private void editRecord() {
        CustomViewAction.performActionAt(0, typeText("doctor"));
        CustomViewAction.performActionAt(2, typeText("patient"));
        CustomViewAction.performActionAt(6,
                CustomViewAction.clickChildViewWithId(R.id.rb_male));
    }

    private void saveRecord() {
        ViewInteraction actionMenuItemView = onView(
                AllOf.allOf(withId(R.id.action_save), withText("保存"), isDisplayed()));
        actionMenuItemView.perform(click());
    }
}
