package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.DialogRecordTypeBinding;
import com.doctor.sun.ui.activity.patient.CreateRecordActivity;

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
                        Intent intent = CreateRecordActivity.makeIntent(context, CreateRecordActivity.TYPE_SELF, isRegister);
                        intent.putExtra(Constants.HANDLER, getMessenger(dialog));
                        context.startActivity(intent);
                        break;
                    }
                    case R.id.tv_relative: {
                        Intent intent = CreateRecordActivity.makeIntent(context, CreateRecordActivity.TYPE_OTHERS, isRegister);
                        intent.putExtra(Constants.HANDLER, getMessenger(dialog));
                        context.startActivity(intent);
                        break;
                    }
                }
            }
        };
        binding.tvCancel.setOnClickListener(listener);
        binding.tvSelf.setOnClickListener(listener);
        binding.tvRelative.setOnClickListener(listener);
        dialog.setCancelable(false);
        dialog.show();
    }

    @NonNull
    private Messenger getMessenger(final Dialog dialog) {
        return new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                dialog.dismiss();
                return false;
            }
        }));
    }
}
