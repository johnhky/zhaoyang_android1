package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.View;

import com.doctor.sun.databinding.ItemSystemMsgBinding;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by rick on 2/2/2016.
 */
public class SystemMsgAdapter extends SimpleAdapter<SystemMsg, ItemSystemMsgBinding> {
    private final long lastVisitTime;

    public SystemMsgAdapter(Context context, long lastVisitTime) {
        super(context);
        this.lastVisitTime = lastVisitTime;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemSystemMsgBinding> vh, int position) {
        super.onBindViewBinding(vh, position);
        SystemMsg systemTip = get(position);
        boolean haveRead = systemTip.getHandler().haveRead(lastVisitTime);
        if (haveRead) {
            vh.getBinding().ivCount.setVisibility(View.GONE);
        } else {
            vh.getBinding().ivCount.setVisibility(View.VISIBLE);
        }
    }
}
