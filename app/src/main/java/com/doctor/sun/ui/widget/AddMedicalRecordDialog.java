package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogRecordTypeBinding;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.ui.activity.patient.EditRecordActivity;

/**
 * Created by rick on 6/1/2016.
 */
public class AddMedicalRecordDialog {
    private Context context;

    public AddMedicalRecordDialog(Context context) {
        this.context = context;
    }

    public void show() {
        DialogRecordTypeBinding binding = DialogRecordTypeBinding.inflate(LayoutInflater.from(context));
        final Dialog dialog = new Dialog(context, R.style.customDialog);
        dialog.setContentView(binding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_cancel: {
                        dialog.dismiss();
                        break;
                    }
                    case R.id.tv_self: {
                        Intent intent = EditRecordActivity.makeIntent(context, EditRecordActivity.TYPE_SELF, isFirstTime());
                        context.startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                    case R.id.tv_relative: {
                        Intent intent = EditRecordActivity.makeIntent(context, EditRecordActivity.TYPE_OTHERS, isFirstTime());
                        context.startActivity(intent);
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
        binding.tvCancel.setOnClickListener(listener);
        binding.tvSelf.setOnClickListener(listener);
        binding.tvRelative.setOnClickListener(listener);
        if (!isFirstTime()) {
            binding.tvSelf.setVisibility(View.GONE);
        }
        dialog.show();
    }

    private boolean isFirstTime() {
        Patient patientProfile = TokenCallback.getPatientProfile();
        if (patientProfile == null) {
            return false;
        }
        return patientProfile.getName().equals("");
    }
}
