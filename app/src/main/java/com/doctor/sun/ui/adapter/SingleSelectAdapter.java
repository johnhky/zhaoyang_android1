package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;


/**
 * Created by rick on 16/3/2016.
 */
public class SingleSelectAdapter extends SimpleAdapter {
    private final OnSelectionChange listener;
    private int selectedPosition = 0;

    public SingleSelectAdapter(OnSelectionChange listener, int selectedPosition) {
        super();
        this.listener = listener;
        this.selectedPosition = selectedPosition;
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
        selectedPosition = vh.getAdapterPosition();
        if (listener != null) {
            listener.onSelectionChange(adapter, selectedPosition);
        }
    }

    public void select(BaseViewHolder vh, BaseListAdapter adapter, boolean shouldSelect) {
        selectedPosition = vh.getAdapterPosition();
        if (listener != null) {
            listener.onSelectionChange(adapter, selectedPosition);
        }
    }

    public void select(int position, BaseListAdapter adapter) {
        selectedPosition = position;
        if (listener != null) {
            listener.onSelectionChange(adapter, selectedPosition);
        }
    }

    public void selectNoCallback(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public boolean isSelected(BaseViewHolder vh) {
        return selectedPosition == vh.getAdapterPosition();
    }

    public interface OnSelectionChange {
        void onSelectionChange(BaseListAdapter adapter, int newSelectItem);
    }
}
