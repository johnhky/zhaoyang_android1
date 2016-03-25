package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.text.Html;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemRecordBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.ui.activity.doctor.ConsultingDetailActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * 病人端历史纪录列表adapter
 * Created by Lynn on 1/14/16.
 */
public class RecordHistoriesAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Context mActivity;
    public RecordHistoriesAdapter(Context context) {
        super(context);
        mActivity = context;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_record) {
            final ItemRecordBinding binding = ((ItemRecordBinding) vh.getBinding());
            binding.setData((AppointMent) get(position));
            binding.tvMoney.setText(Html.fromHtml(
                    getString(R.string.money, ((AppointMent) get(position)).getMoney())));

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.startActivity(ConsultingDetailActivity.makeIntent(getContext(),
                            (AppointMent)get(position), ConsultingDetailActivity.POSITION_SUGGESTION_READONLY));
                }
            });
        }
        super.onBindViewBinding(vh, position);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_record;
    }
}
