package com.doctor.sun.vm;

import android.databinding.Bindable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

import com.doctor.sun.BR;

/**
 * Created by rick on 16/2/2017.
 */

public abstract class BillMenu extends BaseMenu {
    private String totalRevenue;

    public BillMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title) {
        super(itemLayoutId, icon, title);
    }

    @Bindable
    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
        notifyPropertyChanged(BR.totalRevenue);
    }
}
