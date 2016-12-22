package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.SelectAppointmentTypeEvent;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 15/12/2016.
 */

public class ItemStandardAppointment extends BaseItem {

    private double price;

    private boolean selected = false;

    public ItemStandardAppointment(double price) {
        this.price = price;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_standard_appointment;
    }

    public void select() {
        selected = !selected;
        notifyPropertyChanged(BR.selected);

        EventHub.post(new SelectAppointmentTypeEvent(AppointmentType.STANDARD));
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public double getPrice() {
        return price;
    }
}
