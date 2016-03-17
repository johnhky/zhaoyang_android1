package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.handler.MedicineHelperHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by lucas on 1/29/16.
 */
public class MedicineHelper implements LayoutId {
    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_medicine_helper;
    }

    private MedicineHelperHandler handler = new MedicineHelperHandler(this);

    public MedicineHelperHandler getHandler() {
        return handler;
    }

    public void setHandler(MedicineHelperHandler handler) {
        this.handler = handler;
    }
}
