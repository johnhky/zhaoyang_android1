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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Description that = (Description) o;

        return content != null ? content.equals(that.content) : that.content == null;

    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }
}
