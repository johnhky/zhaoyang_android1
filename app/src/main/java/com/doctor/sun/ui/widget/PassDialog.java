package com.doctor.sun.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;

import com.doctor.sun.databinding.DialogPassBinding;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * Created by lucas on 2/19/16.
 */
public class PassDialog extends BaseDialog {
    private Context context;
    private DialogPassBinding binding;

    public PassDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void beforeInitView() {
        binding = DialogPassBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    public void initView() {
        setContentView(binding.getRoot());
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
