package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.event.ShowDialogEvent;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16/12/2016.
 */

public class ItemPickAppointmentDuration extends BaseItem {

    private double price;
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


    public ItemPickAppointmentDuration(double price) {
        this.price = price;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_pick_appointment_time;
    }

    public String getPrice() {
            return price + "元/15分钟";
    }

    public int getAppointmentIcon() {
        return R.drawable.ic_premium_appointment;
    }

    public void selectAppointmentType() {
        EventHub.post(new ShowDialogEvent());
    }
}
