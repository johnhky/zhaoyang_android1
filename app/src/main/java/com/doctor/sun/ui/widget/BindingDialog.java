package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by lucas on 2/19/16.
 */
public class BindingDialog extends Dialog {
    private final LayoutId layoutId;
    private Context context;
    private ViewDataBinding binding;

    public BindingDialog(Context context, LayoutId layoutId) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId.getItemLayoutId(), null, false);
        binding.setVariable(BR.data, layoutId);
        setContentView(binding.getRoot());
    }

    public static MaterialDialog.Builder newBuilder(Context context, LayoutId layoutId) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId.getItemLayoutId(), null, false);
        binding.setVariable(BR.data, layoutId);
        return new MaterialDialog.Builder(context)
                .customView(binding.getRoot(), true);
    }
}
