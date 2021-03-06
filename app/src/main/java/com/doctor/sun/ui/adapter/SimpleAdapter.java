package com.doctor.sun.ui.adapter;

import android.databinding.ViewDataBinding;

import com.doctor.sun.ui.adapter.core.ListAdapter;
import com.doctor.sun.vm.LayoutId;


/**
 * Created by rick on 11/28/15.
 */
public class SimpleAdapter<T extends LayoutId, B extends ViewDataBinding> extends ListAdapter<T, B> {
    public static final String TAG = SimpleAdapter.class.getSimpleName();
    private MapLayoutIdInterceptor layoutIdMap = new MapLayoutIdInterceptor();

    public SimpleAdapter() {
        super();
        setLayoutIdInterceptor(layoutIdMap);
    }

    public final void mapLayout(int from, int to) {
        layoutIdMap.put(from, to);
    }
}
