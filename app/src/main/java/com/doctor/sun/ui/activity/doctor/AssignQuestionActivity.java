package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAssignQuestionBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.QuestionPagerAdapter;


/**
 * Created by rick on 11/28/15.
 */
public class AssignQuestionActivity extends BaseFragmentActivity2 implements AssignQuestionAdapter.GetAppointmentId{
    private ActivityAssignQuestionBinding binding;
    private String appointmentId;

    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, AssignQuestionActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentId = String.valueOf(getData().getId());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_assign_question);

        binding.vp.setAdapter(new QuestionPagerAdapter(getSupportFragmentManager(), appointmentId));

        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);

    }

    @Override
    public String getAppointmentId() {
        return appointmentId;
    }

    @Override
    public int getMidTitle() {
        return R.string.title_add_question;
    }
}
