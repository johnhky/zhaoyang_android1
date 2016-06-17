package com.doctor.sun.vo;

import android.databinding.Bindable;
import android.widget.CompoundButton;

import com.doctor.sun.BR;

/**
 * Created by rick on 12/22/15.
 */
public class ItemSwitch extends BaseItem {
    public ItemSwitch(int layoutId, String content) {
        super(layoutId);
        this.content = content;
    }

    private String content;
    private boolean isChecked = false;
    private OnCheckedChangeListener listener;


    public String getContent() {
        return content;
    }

    @Bindable
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        notifyPropertyChanged(BR.isChecked);
    }

    public CompoundButton.OnCheckedChangeListener getListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onCheckedChanged(isChecked);
                }
                setChecked(isChecked);
            }
        };
    }

    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public int getTintColor() {
        return 0x339de1;
    }


    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }
}
