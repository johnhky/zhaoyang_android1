package com.doctor.sun.vo;

import android.databinding.BaseObservable;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 7/7/2016.
 */

public class ItemLoadMore extends BaseObservable implements SortedItem, Event {

    private boolean isTriggered = false;


    public int loadMore() {
        if (!isTriggered) {
            notifyChange();
            isTriggered = true;
        }
        return R.color.white;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_load_more;
    }


    @Override
    public long getCreated() {
        return Long.MIN_VALUE;
    }

    @Override
    public String getKey() {
        return "ITEM_LOAD_MORE";
    }
}
