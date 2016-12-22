package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.AppointmentType;

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

    private int type;
    private double price;

    public ItemPickAppointmentDuration(int type, double price) {
        this.type = type;
        this.price = price;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_pick_appointment_time;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        if (type == AppointmentType.PREMIUM) {
            return "专属咨询";
        } else {
            return "闲时咨询";
        }
    }

    public int getTitleDrawable() {
        if (type == AppointmentType.PREMIUM) {
            return R.drawable.ic_premium_appointment;
        } else {
            return R.drawable.ic_standard_appointment;
        }
    }

    public String getPrice() {
        if (type == AppointmentType.PREMIUM) {
            return price + "元/15分钟";
        } else {
            return price + "元/每次";
        }
    }
}
