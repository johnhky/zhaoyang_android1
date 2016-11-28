package com.doctor.sun.entity;

import android.content.Context;
import android.support.annotation.ColorRes;

import com.doctor.sun.R;
import com.doctor.sun.util.NameComparator;
import com.doctor.sun.vo.BaseItem;

/**
 * Created by lucas on 12/2/15.
 */
public class Description extends BaseItem implements NameComparator.Name {

    private int layoutId;
    private String content;
    private int indexPosition;

    private int backgroundColor = R.color.color_description_background;
    private int titleColor = R.color.color_description_title;

    public Description(int layoutId) {
        this.layoutId = layoutId;
        this.content = "";
    }

    public Description(int layoutId, String content) {
        this.layoutId = layoutId;
        this.content = content;
    }

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

    public int getIndexPosition() {
        return indexPosition;
    }

    public void setIndexPosition(int indexPosition) {
        this.indexPosition = indexPosition;
    }

    public void setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setTitleColor(@ColorRes int titleColor) {
        this.titleColor = titleColor;
    }

    public int getTitleColor(Context context) {
        return context.getResources().getColor(titleColor);
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
