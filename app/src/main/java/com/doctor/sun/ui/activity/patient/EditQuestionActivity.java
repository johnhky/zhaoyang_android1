package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityFillForumBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;

import retrofit2.Call;

/**
 * 填写问卷 只读 fragment
 * Created by rick on 25/1/2016.
 */
public class EditQuestionActivity extends BaseFragmentActivity2 {


    private ActivityFillForumBinding binding;
    private AnswerQuestionFragment fragment;

    public static Intent intentFor(Context context, String appointmentId, String type) {
        Intent i = new Intent(context, EditQuestionActivity.class);
        i.putExtra(Constants.TYPE, type);
        i.putExtra(Constants.DATA, appointmentId);
        return i;
    }


    private String getData() {
        return getIntent().getStringExtra(Constants.DATA);
    }

    private String getType() {
        return getIntent().getStringExtra(Constants.TYPE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fill_forum);
        initFragment();
        AppointmentModule api = Api.of(AppointmentModule.class);
        api.appointmentDetail(getData()).enqueue(new SimpleCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
                binding.setData(response);
            }

            @Override
            public void onFailure(Call<ApiDTO<Appointment>> call, Throwable t) {
                super.onFailure(call, t);
                }
        });
    }

    private void initFragment() {
        fragment = AnswerQuestionFragment.getInstance(String.valueOf(getData()), getType());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lly_content, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_answer_question;
    }
}
