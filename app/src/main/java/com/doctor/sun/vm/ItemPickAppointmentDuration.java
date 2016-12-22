package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;

/**
 * Created by kb on 16/12/2016.
 */

public class ItemPickAppointmentDuration extends BaseItem {
    private int selectedItem;

    public ItemPickAppointmentDuration() {
    }

    @Bindable
    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyPropertyChanged(BR.selectedItem);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_pick_appointment_time;
    }
}
