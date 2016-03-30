package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 16/3/2016.
 */
public class MultiSelectAdapter extends SimpleAdapter {
    private final OnSelectionChange listener;
    private SparseBooleanArray selectedState = new SparseBooleanArray();

    public MultiSelectAdapter(Context context, OnSelectionChange listener) {
        super(context);
        this.listener = listener;
    }

    public MultiSelectAdapter(Context context, OnSelectionChange listener, SparseBooleanArray initState) {
        super(context);
        this.listener = listener;
        selectedState = initState;
    }

    @Override
    public View.OnClickListener onItemClick(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(vh, adapter);
                notifyDataSetChanged();
            }
        };
    }

    public void select(BaseViewHolder vh, BaseAdapter adapter) {
        int position = vh.getAdapterPosition();
        boolean isSelected = selectedState.get(position);
        selectedState.put(position, !isSelected);
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public void select(BaseViewHolder vh, BaseAdapter adapter,boolean shouldSelect) {
        int position = vh.getAdapterPosition();
        selectedState.put(position, shouldSelect);
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public boolean isSelected(BaseViewHolder vh) {
        return selectedState.get(vh.getAdapterPosition());
    }

    public interface OnSelectionChange {
        void onSelectionChange(BaseAdapter adapter, SparseBooleanArray selectedItems);
    }
}
