package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.SparseIntArray;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.ListAdapter;


/**
 * Created by rick on 11/28/15.
 */
public class SimpleAdapter<T extends LayoutId, B extends ViewDataBinding> extends ListAdapter<T, B> {
    public static final String TAG = SimpleAdapter.class.getSimpleName();
    private SparseIntArray layoutIdMap = new SparseIntArray();

    public SimpleAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<B> vh, int position) {

    }

    @Override
    protected int getItemLayoutId(int position) {
        int itemLayoutId = get(position).getItemLayoutId();
        int i = layoutIdMap.get(itemLayoutId);
        if (i == 0) {
            return itemLayoutId;
        } else {
            return i;
        }
    }

    public final void mapLayout(int from, int to) {
        layoutIdMap.put(from, to);
    }
}
