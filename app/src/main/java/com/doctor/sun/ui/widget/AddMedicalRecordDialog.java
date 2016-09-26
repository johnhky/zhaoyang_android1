package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogRecordTypeBinding;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;

/**
 * Created by rick on 6/1/2016.
 */
public class AddMedicalRecordDialog {
    private final boolean isRegister;
    private Activity context;

    public AddMedicalRecordDialog(Activity context, boolean isRegister) {
        this.context = context;
        this.isRegister = isRegister;
    }

    public void show() {
        DialogRecordTypeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_record_type, null, false);
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
                        Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_SELF));
                        context.startActivity(intent);
                        break;
                    }
                    case R.id.tv_relative: {
                        Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_OTHER));
                        context.startActivity(intent);
                        break;
                    }
                }
            }
        };
        binding.tvCancel.setOnClickListener(listener);
        binding.tvSelf.setOnClickListener(listener);
        binding.tvRelative.setOnClickListener(listener);
        if (isRegister) {
            binding.tvRelative.setText("暂不填写,先逛逛");
        }
        dialog.setCancelable(false);
        dialog.show();
    }
}
