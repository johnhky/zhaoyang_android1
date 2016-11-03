package com.doctor.sun.ui.activity.patient.handler;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;
import com.doctor.sun.vo.LayoutId;

/**
 * Created by lucas on 1/20/16.
 */
public class NewRecordHandler implements LayoutId {
    private Dialog dialog;

    public NewRecordHandler(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_new;
    }

    public void newRecord(View view) {
        Patient patientProfile = Settings.getPatientProfile();
        if (patientProfile == null || "".equals(patientProfile.getName())) {
            Bundle args = NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_SELF);
            Intent intent = SingleFragmentActivity.intentFor(view.getContext(), "新建个人病历", args);
            view.getContext().startActivity(intent);
        } else {
            Bundle args = NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_OTHER);
            Intent intent = SingleFragmentActivity.intentFor(view.getContext(), "新建亲友病历", args);
            view.getContext().startActivity(intent);
        }
        dialog.dismiss();
    }
}
