package com.doctor.sun.ui.activity;

import android.os.SystemClock;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.activity.patient.PMainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by kb on 16-11-15.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestAppointmentProcedure {

    @Rule
    public ActivityTestRule<PMainActivity> mActivityTestRule = new ActivityTestRule<>(PMainActivity.class);

    @Test
    public void testAppointmentProcedure() {

        // 有时会出现更新弹窗
        try {
            SystemClock.sleep(1000);
            onView(withText("稍后提醒我")).perform(click());
        } catch (NoMatchingViewException e) {

        }

        selectAppointmentType();

        selectDoctor();

        selectRecordAndTime();
    }

    private void selectAppointmentType() {
        ViewInteraction appointmentType = onView(
                allOf(withText("预约医生"), isDisplayed())
        );
        appointmentType.perform(click());

        ViewInteraction appointmentType1 = onView(
                allOf(withText("闲时咨询"), isDisplayed())
        );
        appointmentType1.perform(click());

        ViewInteraction appointmentType2 = onView(
                allOf(withText("专属咨询"), isDisplayed())
        );
        appointmentType2.perform(click());

        ViewInteraction next = onView(
                allOf(withText("下一步"), isDisplayed())
        );
        next.perform(click());
    }

    private void selectDoctor() {
        // 等待网络获取数据2秒, 选择医生列表中的第一位医生
        SystemClock.sleep(1000);
        CustomViewAction.performActionAt(0, click());

        onView(allOf(withText("评论"), isDisplayed())).perform(click());
        onView(allOf(withText("文章"), isDisplayed())).perform(click());
        onView(allOf(withText("医生简介"), isDisplayed())).perform(click());

        onView(allOf(withText("闲时咨询"), isDisplayed())).perform(click());
        onView(allOf(withText("专属咨询"), isDisplayed())).perform(click());

        // 专属咨询时长选择
        onView(allOf(withId(R.id.btn_three), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.btn_one), isDisplayed())).perform(click());

        ViewInteraction confirm = onView(
                allOf(withText("确  定"), isDisplayed())
        );
        confirm.perform(click());
    }

    private void selectRecordAndTime() {
        // 选择病历的弹窗，选择第一个病历
        onView(withId(R.id.rv_record)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );
    }
}
