package com.doctor.sun.ui.adapter;

import android.databinding.ViewDataBinding;
import android.util.SparseBooleanArray;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.vo.LayoutId;

/**
 * Created by rick on 16/3/2016.
 */
public class MultiSelectAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private final OnSelectionChange listener;
    private SparseBooleanArray selectedState = new SparseBooleanArray();

    public MultiSelectAdapter() {
        super();
        listener = null;
    }

    public MultiSelectAdapter(OnSelectionChange listener) {
        super();
        this.listener = listener;
    }

    public MultiSelectAdapter(OnSelectionChange listener, SparseBooleanArray initState) {
        super();
        this.listener = listener;
        selectedState = initState;
    }

    public View.OnClickListener onItemClick(final BaseListAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(vh, adapter);
                notifyDataSetChanged();
            }
        };
    }

    public void select(BaseViewHolder vh, BaseListAdapter adapter) {
        int position = vh.getAdapterPosition();
        boolean isSelected = selectedState.get(position);
        selectedState.put(position, !isSelected);
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public void select(BaseViewHolder vh, BaseListAdapter adapter, boolean shouldSelect) {
        int position = vh.getAdapterPosition();
        selectedState.put(position, shouldSelect);
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public boolean isSelected(BaseViewHolder vh) {
        return selectedState.get(vh.getAdapterPosition());
    }

    public boolean isSelected(int position) {
        return selectedState.get(position);
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            selectedState.put(i, true);
        }
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public void unSelectAll() {
        selectedState.clear();
        if (listener != null) {
            listener.onSelectionChange(MultiSelectAdapter.this, selectedState);
        }
    }

    public boolean isAllSelected() {
        for (int i = 0; i < getItemCount(); i++) {
            if (!selectedState.get(i)) {
                return false;
            }
        }
        return true;
    }

    public interface OnSelectionChange {
        void onSelectionChange(BaseListAdapter adapter, SparseBooleanArray selectedItems);
    }
}
