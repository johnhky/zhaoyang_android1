package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityQuestionBankBinding;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 1/19/16.
 */
public class QuestionBankActivity extends BaseActivity2 implements AssignQuestionAdapter.GetAppointmentId {
    private ActivityQuestionBankBinding binding;
    private SimpleAdapter mAdapter;
    private QuestionModule api = Api.of(QuestionModule.class);

    /*public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, QuestionBankActivity.class);
        return i;
    }*/

    public static Intent makeIntent(Context context, String appointmentId) {
        Intent i = new Intent(context, QuestionBankActivity.class);
        i.putExtra(Constants.DATA, appointmentId);
        return i;
    }


    private String appointmentId() {
        return getIntent().getStringExtra(Constants.DATA);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_bank);
        mAdapter = new SimpleAdapter(this);
        HeaderViewModel header = new HeaderViewModel(this);
        binding.setHeader(header);
        mAdapter.mapLayout(R.layout.item_question,R.layout.item_question_extend);
        binding.rvQuestionBank.setLayoutManager(new LinearLayoutManager(this));
        binding.rvQuestionBank.setAdapter(mAdapter);
        api.customs("").enqueue(new PageCallback<Question>(mAdapter));
    }

    @Override
    public String getAppointmentId() {
        return appointmentId();
    }
}
