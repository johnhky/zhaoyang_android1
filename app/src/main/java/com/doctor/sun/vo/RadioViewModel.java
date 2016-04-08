package com.doctor.sun.vo;

import android.databinding.BaseObservable;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;


/**
 * Created by rick on 21/3/2016.
 */
public class RadioViewModel extends BaseObservable implements LayoutId {
    private int itemLayoutId;
    private int selectedItem = 0;

    public RadioViewModel(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyChange();
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    public View.OnClickListener select(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedItem(position);
            }
        };
    }
}
