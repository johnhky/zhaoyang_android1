package com.doctor.sun.entity;

import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 12/22/15.
 */
public abstract class ItemButton extends BaseItem {
    public ItemButton(int layoutId, String content) {
        super(layoutId);
        this.content = content;
    }
    private String content;


    public String getContent() {
        return content;
    }


    public abstract void onClick(View view);
}
