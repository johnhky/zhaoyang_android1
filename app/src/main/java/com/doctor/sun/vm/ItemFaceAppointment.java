package com.doctor.sun.vm;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.SelectAppointmentTypeEvent;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by heky on 17/4/14.
 */

public class ItemFaceAppointment extends BaseItem {

    private double price;

    public ItemFaceAppointment(double price) {
        this.price = price;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_face_appointment;
    }

    public void select(BaseListAdapter adapter) {
        adapter.putInt(AdapterConfigKey.APPOINTMENT_TYPE, AppointmentType.FACE);
        adapter.notifyDataSetChanged();
        EventHub.post(new SelectAppointmentTypeEvent(AppointmentType.FACE));
    }

    public boolean isSelected(BaseListAdapter adapter) {
        return adapter.getInt(AdapterConfigKey.APPOINTMENT_TYPE) == AppointmentType.FACE;
    }

    public double getPrice() {
        return price;
    }

    public int getAppointmentBackground() {
        return R.drawable.ic_face_appointment;
    }

    public int getDoneIcon() {
        return R.drawable.ic_done;
    }

    public int triangle(BaseListAdapter adapter) {
        if (isSelected(adapter)) {
            return R.drawable.vector_top_right_triangle_blue;
        } else {
            return R.drawable.vector_top_right_triangle;
        }
    }
}
