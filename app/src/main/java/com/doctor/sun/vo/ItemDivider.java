package com.doctor.sun.vo;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 31/3/2016.
 */
public class ItemDivider implements LayoutId {
    private int itemLayoutId;

    public ItemDivider(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }
}
