package com.doctor.sun.vm;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Try;

import java.util.HashMap;

/**
 * Created by rick on 7/7/2016.
 */

public class ItemLoadMore extends BaseItem {

    private Try aTry;
    private String id = "ITEM_LOAD_MORE";

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

    public void setId(String id) {
        this.id = id;
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
        return id;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public int getSpan() {
        return 12;
    }

    @Override
    public boolean isUserSelected() {
        return false;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }
}
