package com.doctor.sun.ui.adapter.ViewHolder;

import com.doctor.sun.ui.adapter.core.SortedListAdapter;

/**
 * Created by rick on 11/24/15.
 */
public interface SortedItem  {
    int getLayoutId();

    long getCreated();

    String getKey();

    float getSpan();

    String toJson(SortedListAdapter adapter);
}
