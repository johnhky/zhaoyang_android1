package com.doctor.sun.vo;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;


/**
 * Created by rick on 11/3/2016.
 */
public class MenuViewModel extends BaseObservable implements LayoutId {
    private int icon;
    private int requestCode = -1;
    private String title;
    private String subTitle;
    private String detail;
    private final Class<?> clazz;
    private Parcelable parcelable;
    private int itemLayoutId;

    public MenuViewModel(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title, String status, Class<?> clazz) {
        this.itemLayoutId = itemLayoutId;
        this.icon = icon;
        this.title = title;
        this.subTitle = status;
        this.clazz = clazz;
    }

    public MenuViewModel(@DrawableRes int icon, String title, String status, Class<?> clazz) {
        this(R.layout.item_menu2, icon, title, status, clazz);
    }

    public MenuViewModel(@DrawableRes int icon, String title, String status) {
        this(R.layout.item_menu2, icon, title, status, null);
    }

    public MenuViewModel(@DrawableRes int icon, String title) {
        this(R.layout.item_menu2, icon, title, "", null);
    }

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

    public void setParcelable(Parcelable parcelable) {
        this.parcelable = parcelable;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void navigate(View v) {
        if (clazz == null) return;
        Intent i = new Intent();
        Activity context = (Activity) v.getContext();
        i.setClass(context, clazz);
        if (parcelable != null) {
            i.putExtra(Constants.DATA, parcelable);
        }
        if (requestCode > 0) {
            context.startActivityForResult(i, requestCode);
        } else {
            context.startActivity(i);
        }
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

}
