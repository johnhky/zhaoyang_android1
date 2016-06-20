package com.doctor.sun.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.ListAdapter;

/**
 * Created by rick on 24/12/2015.
 */
public class BaseItem extends BaseObservable implements LayoutId {

    private boolean visible = true;
    private int itemLayoutId;
    private int position;

    public BaseItem() {
    }

    public BaseItem(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyChange();
    }

    @Bindable
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }
}
