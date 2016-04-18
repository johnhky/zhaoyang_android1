package com.doctor.sun.vo;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doctor.sun.databinding.IncludeInputLayoutBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.event.HideInputEvent;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;

/**
 * Created by rick on 13/4/2016.
 */
public class InputLayoutViewModel extends BaseObservable {
    private static EditText inputTextView;
    private IncludeInputLayoutBinding binding;
    private Appointment data;
    private boolean recordMode = false;
    private RecordAudioViewModel recordAudioViewModel;

    public InputLayoutViewModel(final IncludeInputLayoutBinding binding, Appointment data) {
        this.binding = binding;
        this.data = data;
        recordAudioViewModel = new RecordAudioViewModel(binding.getRoot().getContext());
        binding.setAudioVM(recordAudioViewModel);
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

    public View.OnClickListener onAudioBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecordMode(!isRecordMode());
                binding.setData(InputLayoutViewModel.this);
                binding.setIsEditing(false);
                Activity context = (Activity) v.getContext();
                if (!isRecordMode()) {
                    binding.inputText.requestFocus();
                    Systems.showKeyboard(context.getWindow(), binding.inputText);
                } else {
                    EventHub.post(new HideInputEvent());
                    Systems.hideKeyboard(context);
                }
            }
        };
    }


    public boolean isRecordMode() {
        return recordMode;
    }

    public void setRecordMode(boolean recordMode) {
        this.recordMode = recordMode;
        notifyChange();
    }

}
