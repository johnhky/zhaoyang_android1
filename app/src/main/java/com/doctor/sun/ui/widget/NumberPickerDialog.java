package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.doctor.sun.databinding.ItemPickNumberBinding;


/**
 * Created by kb on 16-12-2.
 */

public class NumberPickerDialog {
    private Activity context;
    private int min;
    private int max;
    private View.OnClickListener confirm;

    private Dialog dialog;
    private int selected = 28;

    public NumberPickerDialog(Activity context, int min, int max) {
        this.context = context;
        this.min = min;
        this.max = max;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }

        ItemPickNumberBinding binding = ItemPickNumberBinding.inflate(LayoutInflater.from(context));
        if (confirm != null) {
            binding.confirm.setOnClickListener(confirm);
        }
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        NumberPicker numberPicker = binding.numberPicker;
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(selected);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selected = newVal;
            }
        });

        dialog = BottomDialog.showDialog(context, binding.getRoot());
    }

    public void setConfirm(View.OnClickListener confirm) {
        this.confirm = confirm;
    }

    public void setValue(int value) {
        selected = value;
    }

    public int getValue() {
        return selected;
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
