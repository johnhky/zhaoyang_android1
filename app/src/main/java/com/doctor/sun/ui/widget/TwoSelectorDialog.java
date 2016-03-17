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
public class TwoSelectorDialog extends BaseDialog {
    private Context context;
    private DialogDeleteBinding binding;
    private GetActionButton button;
    private String question;
    private String cancel;
    private String apply;

    public TwoSelectorDialog(Context context, String question, String cancel,
                             String apply, final GetActionButton button) {
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
                button.onClickPositiveButton(TwoSelectorDialog.this);
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.onClickNegativeButton(TwoSelectorDialog.this);
            }
        });
    }

    @Override
    public void initData() {
        binding.tvTitle.setText(question);
        binding.tvCancel.setText(cancel);
        binding.tvDelete.setText(apply);
    }

    public interface GetActionButton {
        void onClickPositiveButton(TwoSelectorDialog dialog);

        void onClickNegativeButton(TwoSelectorDialog dialog);
    }


    public static void showTwoSelectorDialog(Context context, String question, String cancel, String apply, final GetActionButton button) {
        final TwoSelectorDialog deleteDialog = new TwoSelectorDialog(context, question, cancel,
                apply, button);
        deleteDialog.show();
    }

    public static void showDialog(Context context, String question, String cancel, String apply, final GetActionButton button) {
        final TwoSelectorDialog deleteDialog = new TwoSelectorDialog(context, question, cancel,
                apply, button);
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show();
    }
}
