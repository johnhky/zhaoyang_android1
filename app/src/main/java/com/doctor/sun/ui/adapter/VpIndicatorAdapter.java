package com.doctor.sun.ui.adapter;

import android.databinding.ViewDataBinding;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.vo.EmptyLayoutId;
import com.doctor.sun.vo.LayoutId;

/**
 * Created by rick on 8/4/2016.
 */
public class VpIndicatorAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private int itemCount = 0;
    private int selectedPosition = 0;

    public VpIndicatorAdapter() {
        super();
        onFinishLoadMore(true);
    }


    @Override
    public int getItemViewType(int position) {
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
