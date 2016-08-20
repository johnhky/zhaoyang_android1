package com.doctor.sun.vo;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.doctor.sun.BR;

/**
 * Created by rick on 7/4/2016.
 */
public abstract class BaseMenu extends BaseItem {
    protected int itemLayoutId;
    protected int icon;
    protected String title;
    protected String subTitle;
    protected String detail;
    protected boolean enable = true;

    public BaseMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title) {
        this.itemLayoutId = itemLayoutId;
        this.title = title;
        this.icon = icon;
    }

    @DrawableRes
    @Bindable
    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }

    @Bindable
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        notifyPropertyChanged(BR.detail);
    }

    @Bindable
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        notifyPropertyChanged(BR.enable);
    }

    public abstract View.OnClickListener itemClick();

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }
}
