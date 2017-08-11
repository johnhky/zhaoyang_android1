package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogRecordTypeBinding;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.CreateNewMedicineReordActivity;
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

//    public void show() {
//        DialogRecordTypeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_record_type, null, false);
//        final Dialog dialog = new Dialog(context, R.style.customDialog);
//        dialog.setContentView(binding.getRoot());
//        dialog.setCanceledOnTouchOutside(false);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.tv_cancel: {
//                        dialog.dismiss();
//                        break;
//                    }
//                    case R.id.tv_self: {
//                        Bundle args = NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_SELF);
//                        Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", args);
//                        context.startActivity(intent);
//                        dialog.dismiss();
//                        break;
//                    }
//                    case R.id.tv_relative: {
//                        Bundle args = NewMedicalRecordFragment.getArgs(NewMedicalRecordFragment.TYPE_OTHER);
//                        Intent intent = SingleFragmentActivity.intentFor(context, "新建病历", args);
//                        context.startActivity(intent);
//                        dialog.dismiss();
//                        break;
//                    }
//                }
//            }
//        };
//        binding.tvCancel.setOnClickListener(listener);
//        binding.tvSelf.setOnClickListener(listener);
//        binding.tvRelative.setOnClickListener(listener);
//        if (isRegister) {
//            binding.tvCancel.setText("暂不填写,先逛逛");
//        }
//        dialog.setCancelable(false);
//        dialog.show();
//    }

    public void show(boolean hasSelfRecord) {
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
                        Intent toCreate = new Intent();
                        toCreate.setClass(context, CreateNewMedicineReordActivity.class);
                        context.startActivity(toCreate);
                        dialog.dismiss();
                        break;
                    }
                    case R.id.tv_relative: {
                        Intent toCreate = new Intent();
                        toCreate.setClass(context, CreateNewMedicineReordActivity.class);
                        context.startActivity(toCreate);
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };
        binding.tvCancel.setOnClickListener(listener);
        binding.tvSelf.setOnClickListener(listener);
        binding.tvRelative.setOnClickListener(listener);
        if (isRegister) {
            binding.tvCancel.setText("暂不填写,先逛逛");
        }
        if (hasSelfRecord) {
            binding.tvSelf.setText("本人病历(已建立)");
            binding.tvSelf.setEnabled(false);
        }
        dialog.setCancelable(false);
        dialog.show();
    }
}
