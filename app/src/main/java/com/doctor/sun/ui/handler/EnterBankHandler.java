package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.doctor.QuestionBankActivity;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by lucas on 1/19/16.
 */
public class EnterBankHandler extends BaseHandler implements LayoutId{
    public EnterBankHandler(Activity context) {
        super(context);
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_enter_bank;
    }

    public void enterQuestionBank(View view){
        AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId)view.getContext();
        String appointmentId = getAppointmentId.getAppointmentId();
        Intent intent = QuestionBankActivity.makeIntent(view.getContext(),appointmentId);
        view.getContext().startActivity(intent);
    }
}
