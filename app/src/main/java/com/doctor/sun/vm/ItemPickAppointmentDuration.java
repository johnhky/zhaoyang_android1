package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.ShowDialogEvent;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16/12/2016.
 */

public class ItemPickAppointmentDuration extends BaseItem {

    private int selectedItem;
    private AppointmentBuilder builder;

    public ItemPickAppointmentDuration(AppointmentBuilder builder) {
        this.builder = builder;
    }

    @Bindable
    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        builder.setDurationNotifyAll(selectedItem * 15);
        notifyPropertyChanged(BR.selectedItem);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_pick_appointment_time;
    }

    public String getPrice() {
        switch (builder.getType()) {
            case AppointmentType.PREMIUM:
                return builder.price() + "元/15分钟";
            case AppointmentType.STANDARD:
                return builder.price() + "元/天(医生空闲时间)";
            default:
                return builder.price() + "元/15分钟";
        }
    }

    public int getAppointmentIcon() {
        switch (builder.getType()) {
            case AppointmentType.PREMIUM:
                return R.drawable.ic_premium_appointment;
            case AppointmentType.STANDARD:
                return R.drawable.ic_standard_appointment;
            default:
                return R.drawable.ic_premium_appointment;
        }
    }

    public void selectAppointmentType() {
        EventHub.post(new ShowDialogEvent());
    }

    public boolean isPremium() {
        return builder.getType() == AppointmentType.PREMIUM;
    }

    public String getTypeLabel() {
        return builder.getTypeLabel();
    }
}
