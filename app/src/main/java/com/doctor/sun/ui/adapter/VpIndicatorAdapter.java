package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.EmptyLayoutId;

/**
 * Created by rick on 8/4/2016.
 */
public class VpIndicatorAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private int itemCount = 0;
    private int selectedPosition = 0;

    public VpIndicatorAdapter(Context context) {
        super(context);
        onFinishLoadMore(true);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_vp_indicator;
    }

    public boolean isSelected(BaseViewHolder vh) {
        return selectedPosition == vh.getAdapterPosition();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    @Override
    public LayoutId get(int location) {
        return new EmptyLayoutId();
    }
}
