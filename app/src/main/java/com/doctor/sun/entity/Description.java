package com.doctor.sun.entity;

import android.databinding.BaseObservable;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.NameComparator;

/**
 * Created by lucas on 12/2/15.
 */
public class Description extends BaseObservable implements LayoutId ,NameComparator.Name{
    public Description(int layoutId, String content) {
        this.layoutId = layoutId;
        this.content = content;
    }

    private int layoutId;
    private String content;
    private boolean visible = true;

    public String getContent() {
        return content;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }

    @Override
    public String getName() {
        return getContent();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
