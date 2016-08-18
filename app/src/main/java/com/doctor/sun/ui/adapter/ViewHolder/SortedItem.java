package com.doctor.sun.ui.adapter.ViewHolder;

import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.HashMap;

/**
 * Created by rick on 11/24/15.
 */
public interface SortedItem {
    int getLayoutId();

    long getCreated();

    String getKey();

    String getValue();

    int getSpan();

    boolean isUserSelected();

    HashMap<String, Object> toJson(SortedListAdapter adapter);
}
