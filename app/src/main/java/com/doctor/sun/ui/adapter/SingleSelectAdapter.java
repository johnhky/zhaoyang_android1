package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;


/**
 * Created by rick on 16/3/2016.
 */
public class SingleSelectAdapter extends SimpleAdapter {
    private final OnSelectionChange listener;
    private int selectedPosition = 0;

    public SingleSelectAdapter(Context context, OnSelectionChange listener, int selectedPosition) {
        super(context);
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public View.OnClickListener onItemClick(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = vh.getAdapterPosition();
                if (listener != null) {
                    listener.onSelectionChange(adapter, selectedPosition);
                }
                notifyDataSetChanged();
            }
        };
    }

    public boolean isSelected(BaseViewHolder vh) {
        return selectedPosition == vh.getAdapterPosition();
    }

    public interface OnSelectionChange {
        void onSelectionChange(BaseAdapter adapter, int newSelectItem);
    }
}
