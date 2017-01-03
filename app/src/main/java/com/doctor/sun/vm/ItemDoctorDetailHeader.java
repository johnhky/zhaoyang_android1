package com.doctor.sun.vm;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;

/**
 * Created by kb on 13/12/2016.
 */

public class ItemDoctorDetailHeader extends BaseItem implements LayoutId{

    private Doctor doctor;

    public ItemDoctorDetailHeader(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_doctor_detail_header;
    }
}
