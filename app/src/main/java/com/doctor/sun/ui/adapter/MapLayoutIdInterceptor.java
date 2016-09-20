package com.doctor.sun.ui.adapter;

import android.util.SparseIntArray;

import com.doctor.sun.ui.adapter.core.BaseListAdapter;

/**
 * Created by rick on 19/9/2016.
 */
public class MapLayoutIdInterceptor implements BaseListAdapter.LayoutIdInterceptor {
    SparseIntArray ids = new SparseIntArray();

    @Override
    public int intercept(int origin) {
        return ids.get(origin, origin);
    }

    public void put(int key, int value) {
        ids.put(key, value);
    }
}
