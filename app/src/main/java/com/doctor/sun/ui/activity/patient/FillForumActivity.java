package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityFillForumBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.FillForumFragment;
import com.doctor.sun.ui.fragment.ModifyForumFragment;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.PickImageDialog;

/**
 * 填写问卷 只读 fragment
 * Created by rick on 25/1/2016.
 */
public class FillForumActivity extends BaseFragmentActivity2 implements
        QCategoryHandler.QCategoryCallback,
        Appointment.AppointmentId,
        Prescription.UrlToLoad {

    private boolean isFilling;

    private ActivityFillForumBinding binding;
    private AnswerQuestionFragment fragment;

    public static Intent makeIntent(Context context, int appointmentId) {
        Intent i = new Intent(context, FillForumActivity.class);
        i.putExtra(Constants.DATA, appointmentId);
        return i;
    }


    private int getData() {
        return getIntent().getIntExtra(Constants.DATA, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fill_forum);
        initFragment();
    }

    private void initFragment() {
        fragment = ModifyForumFragment.getInstance(getData());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lly_content, fragment)
                .commit();
    }



    @Override
    public void onCategorySelect(QuestionCategory data) {
//        binding.getHeader().setRightTitle("保存");
    }

//    public void onMenuClicked() {
//        if (isFilling) {
//            fragment.save();
//        }
//    }
//
//    @Override
//    public void setHeaderRightTitle(String title) {
//        binding.getHeader().setRightTitle(title);
//    }



    @Override
    public int getId() {
        return getData();
    }

    @Override
    public String url() {
        return "diagnosis/last-drug?appointmentId=" + getId();
    }

    @Override
    public int getMidTitle() {
        return R.string.title_answer_question;
    }
}
