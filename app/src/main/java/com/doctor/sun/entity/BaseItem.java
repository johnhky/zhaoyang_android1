package com.doctor.sun.entity;

import android.databinding.BaseObservable;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 24/12/2015.
 */
public class BaseItem extends BaseObservable implements LayoutId {

    private boolean visible = true;
    private int itemLayoutId;

    public BaseItem(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyChange();
    }
}
