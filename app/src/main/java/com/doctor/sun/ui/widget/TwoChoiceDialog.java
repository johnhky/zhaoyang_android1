package com.doctor.sun.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.databinding.DialogDeleteBinding;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * 是否简捷复诊对话框
 * Created by lucas on 12/22/15.
 */
public class TwoChoiceDialog extends BaseDialog {
    private Context context;
    private DialogDeleteBinding binding;
    private Options button;
    private String question;
    private String cancel;
    private String apply;

    public TwoChoiceDialog(Context context, String question, String cancel,
                           String apply, final Options button) {
        super(context);
        this.context = context;
        this.question = question;
        this.cancel = cancel;
        this.apply = apply;
        this.button = button;
    }

    @Override
    public void beforeInitView() {
        binding = DialogDeleteBinding.inflate(LayoutInflater.from(context));

    }

    @Override
    public void initView() {
        setContentView(binding.getRoot());
    }

    @Override
    public void initListener() {
        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.onApplyClick(TwoChoiceDialog.this);
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.onCancelClick(TwoChoiceDialog.this);
            }
        });
    }

    @Override
    public void initData() {
        binding.tvTitle.setText(question);
        binding.tvCancel.setText(cancel);
        binding.tvDelete.setText(apply);
    }

    public interface Options {
        void onApplyClick(TwoChoiceDialog dialog);

        void onCancelClick(TwoChoiceDialog dialog);
    }


    public static void show(Context context, String question, String cancel, String apply, final Options button) {
        final TwoChoiceDialog deleteDialog = new TwoChoiceDialog(context, question, cancel,
                apply, button);
        deleteDialog.show();
    }

    public static void showDialog(Context context, String question, String cancel, String apply, final Options button) {
        final TwoChoiceDialog deleteDialog = new TwoChoiceDialog(context, question, cancel,
                apply, button);
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show();
    }
}
