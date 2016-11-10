package com.doctor.sun.ui.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.FeeActivity;
import com.doctor.sun.ui.activity.doctor.RecordPoolActivity;
import com.doctor.sun.ui.activity.doctor.SettingActivity;
import com.doctor.sun.ui.activity.doctor.TimeActivity;
import com.doctor.sun.ui.activity.doctor.ViewFeedbackActivity;
import com.doctor.sun.ui.activity.patient.MyQrCodeActivity;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.fragment.QTemplatesFragment;

/**
 * Created by lucas on 12/4/15.
 */
public class MeHandler {
    private Doctor data;

    public MeHandler(Doctor doctor) {
        data = doctor;
    }

    public void head(View view) {
        Intent intent = EditDoctorInfoFragment.intentFor(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void Time(View view) {
        Intent intent = TimeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Price(View view) {
        Intent intent = FeeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void recordPool(Context context) {
        Intent intent = RecordPoolActivity.intentFor(context);
        context.startActivity(intent);
    }

    public void Template(View view) {
        Bundle args = QTemplatesFragment.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(view.getContext(), "问卷模版", args);
        view.getContext().startActivity(intent);
    }

    public void Setting(View view) {
        Intent intent = SettingActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void myQrCode(View view) {
        Intent intent = MyQrCodeActivity.intentFor(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void viewFeedback(Context context) {
        Intent intent = ViewFeedbackActivity.intentFor(context, data);
        context.startActivity(intent);
    }

}
