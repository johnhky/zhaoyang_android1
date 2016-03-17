package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.View;

import com.doctor.sun.databinding.ItemSystemTipBinding;
import com.doctor.sun.entity.SystemTip;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by rick on 2/2/2016.
 */
public class SystemTipAdapter extends SimpleAdapter<SystemTip, ItemSystemTipBinding> {
    private final long lastVisitTime;

    public SystemTipAdapter(Context context, long lastVisitTime) {
        super(context);
        this.lastVisitTime = lastVisitTime;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemSystemTipBinding> vh, int position) {
        super.onBindViewBinding(vh, position);
        SystemTip systemTip = get(position);
        boolean haveRead = systemTip.getHandler().haveRead(lastVisitTime);
        if (haveRead) {
            vh.getBinding().ivCount.setVisibility(View.GONE);
        } else {
            vh.getBinding().ivCount.setVisibility(View.VISIBLE);
        }
    }
}
