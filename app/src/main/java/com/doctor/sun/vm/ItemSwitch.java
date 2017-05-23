package com.doctor.sun.vm;

import android.databinding.Bindable;
import android.view.View;

import com.doctor.sun.BR;

/**
 * Created by rick on 12/22/15.
 */
public class ItemSwitch extends BaseItem{
    private String content;
    private boolean isChecked = false;
    private View.OnClickListener listener;
    public ItemSwitch(int layoutId) {
        super(layoutId);
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
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

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        notifyPropertyChanged(BR.clickListener);
    }
}
