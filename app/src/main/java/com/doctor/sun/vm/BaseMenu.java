package com.doctor.sun.vm;

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
    protected String subTitle;
    protected String detail;

    public BaseMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title) {
        this.itemLayoutId = itemLayoutId;
        setTitle(title);
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

    @Override
    public String getValue() {
        return "ignored";
    }

    public abstract View.OnClickListener itemClick();

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

}
