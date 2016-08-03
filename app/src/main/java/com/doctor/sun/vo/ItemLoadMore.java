package com.doctor.sun.vo;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Try;

import java.util.HashMap;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 7/7/2016.
 */

public class ItemLoadMore implements SortedItem, Event {

    private Try aTry;

    public void setLoadMoreListener(Try aTry) {
        this.aTry = aTry;
    }

    public int loadMore() {
        try {
            aTry.success();
        } catch (Exception e) {
            aTry.fail();
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

    @Override
    public float getSpan() {
        return 12;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }
}
