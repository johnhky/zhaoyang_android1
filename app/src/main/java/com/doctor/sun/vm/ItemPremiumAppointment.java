package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.SelectAppointmentTypeEvent;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 15/12/2016.
 */

public class ItemPremiumAppointment extends BaseItem {

    private double price;


    public ItemPremiumAppointment(double price) {
        this.price = price;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_premium_appointment;
    }

    public void select(BaseListAdapter adapter) {
        adapter.putInt(AdapterConfigKey.APPOINTMENT_TYPE,AppointmentType.PREMIUM);
        adapter.notifyDataSetChanged();
        EventHub.post(new SelectAppointmentTypeEvent(AppointmentType.PREMIUM));
    }

    public boolean isSelected(BaseListAdapter adapter) {
        return adapter.getInt(AdapterConfigKey.APPOINTMENT_TYPE) == AppointmentType.PREMIUM;
    }

    public double getPrice() {
        return price;
    }

    public int getAppointmentBackground() {
        return R.drawable.ic_premium_appointment;
    }

    public int getDoneIcon() {
        return R.drawable.ic_done;
    }

    public int triangle(BaseListAdapter adapter) {
        if (isSelected(adapter)) {
            return R.drawable.shape_top_right_triangle_blue;
        } else {
            return R.drawable.shape_top_right_triangle;
        }
    }
}
