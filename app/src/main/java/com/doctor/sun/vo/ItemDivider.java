package com.doctor.sun.vo;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 31/3/2016.
 */
public class ItemDivider implements LayoutId {
    private int itemLayoutId;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ItemDivider(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public ItemDivider(int itemLayoutId, String content) {
        this.itemLayoutId = itemLayoutId;
        this.content = content;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }
}
