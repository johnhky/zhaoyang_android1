package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;

/**
 * Created by kb on 15/12/2016.
 */

public class ItemStandardAppointment extends BaseItem{

    private boolean selected = false;

    @Override
    public int getItemLayoutId() {
        return R.layout.item_standard_appointment;
    }

    public void select() {
        selected = !selected;
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }
}
