package com.doctor.sun.event;

import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vm.BaseItem;

import java.util.List;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 23/12/2016.
 */

public class AdapterItemsEvent implements Event {

    private List<SortedItem> itemList;

    public AdapterItemsEvent(List<SortedItem> itemList) {
        this.itemList = itemList;
    }

    public List<SortedItem> getItemList() {
        return itemList;
    }
}
