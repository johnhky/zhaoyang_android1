package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.handler.MedicineStoreHandler;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by lucas on 1/29/16.
 * 寄药小助手item,只是负责跳转页面,
 * TODO 写个跳转页面的item替换掉
 */
public class MedicineStore implements LayoutId {
    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_medicine_helper;
    }

    private MedicineStoreHandler handler = new MedicineStoreHandler(this);

    public MedicineStoreHandler getHandler() {
        return handler;
    }

    public void setHandler(MedicineStoreHandler handler) {
        this.handler = handler;
    }
}
