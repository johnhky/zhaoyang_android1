package com.doctor.sun.ui.activity;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.ui.activity.action.CustomViewAction;
import com.doctor.sun.ui.fragment.PayPrescriptionsFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kb on 16-9-22.
 */

@RunWith(AndroidJUnit4.class)
public class Test6PayPrescriptionFragment {

    @Rule
    public ActivityTestRule<TestFragmentActivity> mActivityTestRule = new ActivityTestRule<>(TestFragmentActivity.class, false, false);

    @Test
    public void test6PayPrescriptionFragment() {

        Drug drug = new Drug();
        drug.setTo("小明");
        drug.setAddress("七喜创意园");
        drug.setPhone("11111");
        drug.setId(12);
        List<String> drugs = new ArrayList<>();
        drugs.add("药");
        drug.setDrug(drugs);
        drug.setMoney("100");
        drug.setHasPay(0);
        Doctor doctor = new Doctor();
        doctor.setName("老王");
        doctor.setHospitalName("医院");
        doctor.setLevel("高级");
        doctor.setSpecialist("专家");
        doctor.setTitle("兽医");
        drug.setDoctor(doctor);

        Intent intent = new Intent();
        intent.putExtra(Constants.FRAGMENT_CONTENT_BUNDLE, PayPrescriptionsFragment.getArgs(drug));
        mActivityTestRule.launchActivity(intent);

//        onView(withId(R.id.recycler_view))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(6,
//                        CustomViewAction.clickChildViewWithId(R.id.et_select)));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(8,
                        CustomViewAction.clickChildViewWithId(R.id.rb_alipay)));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(9,
                        CustomViewAction.clickChildViewWithId(R.id.tv_confirm)));

    }
}
