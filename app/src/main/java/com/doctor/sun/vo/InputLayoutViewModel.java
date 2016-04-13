package com.doctor.sun.vo;

import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doctor.sun.databinding.IncludeInputLayoutBinding;
import com.doctor.sun.entity.Appointment;

/**
 * Created by rick on 13/4/2016.
 */
public class InputLayoutViewModel {
    private static EditText inputTextView;
    private IncludeInputLayoutBinding binding;
    private Appointment data;

    public InputLayoutViewModel(IncludeInputLayoutBinding binding, Appointment data) {
        this.binding = binding;
        this.data = data;
        setInputTextView(binding.inputText);
    }

    public static EditText getInputTextView() {
        return inputTextView;
    }

    public static void setInputTextView(EditText inputTextView) {
        InputLayoutViewModel.inputTextView = inputTextView;
    }

    public TextViewBindingAdapter.AfterTextChanged afterInputChanged() {
        return new TextViewBindingAdapter.AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.setIsEditing(s.length() > 0);
                binding.executePendingBindings();
            }
        };
    }

    public View.OnClickListener onSendClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.getHandler().sendMessage(binding.inputText);
            }
        };
    }

    public TextView.OnEditorActionListener sendMessageAction() {
        return data.getHandler().sendMessageAction();
    }
}
