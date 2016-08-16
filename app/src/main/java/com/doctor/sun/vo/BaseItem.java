package com.doctor.sun.vo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.HashMap;

/**
 * Created by rick on 24/12/2015.
 */
public class BaseItem extends BaseObservable implements LayoutId, SortedItem {

    private String itemId;

    private boolean userSelected = false;
    private boolean enabled = true;
    private int itemLayoutId;
    private long position;
    private int span = 12;

    public BaseItem() {
    }

    public BaseItem(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public void removeThis(SortedListAdapter adapter) {
        notifyPropertyChanged(BR.removed);
        adapter.removeItem(this);
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyChange();
    }

    @Bindable
    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getLayoutId() {
        return getItemLayoutId();
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return itemId;
    }

    @Override
    public int getSpan() {
        return span;
    }

    @Override
    @Bindable
    public boolean isUserSelected() {
        return userSelected;
    }

    public void setUserSelected(boolean userSelected) {
        this.userSelected = userSelected;
        notifyPropertyChanged(BR.userSelected);
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }
}
