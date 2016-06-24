package com.doctor.sun.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 是否简捷复诊对话框
 * Created by lucas on 12/22/15.
 */
public class TwoChoiceDialog {
//    private Context context;
//    private DialogDeleteBinding binding;
//    private Options button;
//    private String question;
//    private String cancel;
//    private String apply;
//
//    public TwoChoiceDialog(Context context, String question, String cancel,
//                           String apply, final Options button) {
//        super(context);
//        this.context = context;
//        this.question = question;
//        this.cancel = cancel;
//        this.apply = apply;
//        this.button = button;
//    }
//
//    @Override
//    public void beforeInitView() {
//        binding = DialogDeleteBinding.inflate(LayoutInflater.from(context));
//
//    }
//
//    @Override
//    public void initView() {
//        setContentView(binding.getRoot());
//    }

//    @Override
//    public void initListener() {
//        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                button.onApplyClick(TwoChoiceDialog.this);
//            }
//        });
//
//        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                button.onCancelClick(TwoChoiceDialog.this);
//            }
//        });
//    }
//
//    @Override
//    public void initData() {
//        binding.tvTitle.setText(question);
//        binding.tvCancel.setText(cancel);
//        binding.tvDelete.setText(apply);
//    }

    public interface Options {
        void onApplyClick(MaterialDialog dialog);

        void onCancelClick(MaterialDialog dialog);
    }


    public static void show(Context context, String question, String cancel, String apply, final Options button) {
//        final TwoChoiceDialog deleteDialog = new TwoChoiceDialog(context, question, cancel,
//                apply, button);
//        deleteDialog.show();
        showDialog(context, question, cancel, apply, button);
    }

    public static void showDialog(Context context, String question, String cancel, String apply, final Options button) {
//        final TwoChoiceDialog deleteDialog = new TwoChoiceDialog(context, question, cancel,
//                apply, button);
//        deleteDialog.setCanceledOnTouchOutside(false);
//        deleteDialog.show();
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
        builder.show();
    }
}
