package com.doctor.sun.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 是否闲时咨询对话框
 * Created by lucas on 12/22/15.
 */
public class TwoChoiceDialog {


    public interface Options {
        void onApplyClick(MaterialDialog dialog);

        void onCancelClick(MaterialDialog dialog);
    }


    public static void show(Context context, String question, String cancel, String apply, final Options button) {
        showDialog(context, question, cancel, apply, button);
    }

    public static void showDialog(Context context, String question, String cancel, String apply, final Options button) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText(apply)
                .content(question)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        button.onApplyClick(dialog);
                    }
                });

        if (cancel != null) {
            builder.negativeText(cancel)
                    .negativeColor(Color.parseColor("#777777"))
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            button.onCancelClick(dialog);
                        }
                    });
        }
        if (apply.equals("删除")) {
            builder.positiveColor(Color.parseColor("#f04c62"));
        }
        builder.show();
    }

    public static void showDialogUncancelable(Context context, String question, String cancel, String apply, final Options button) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.negativeText(cancel)
                .positiveText(apply)
                .content(question)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        button.onApplyClick(dialog);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        button.onCancelClick(dialog);
                    }
                })
                .negativeColor(Color.parseColor("#777777"));
        if (apply.equals("删除")) {
            builder.positiveColor(Color.parseColor("#f04c62"));
        }
        builder.cancelable(false).show();
    }
}
