package com.doctor.sun.ui.adapter;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemDisturbBinding;
import com.doctor.sun.ui.activity.doctor.BreakTimeActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by lucas on 12/15/15.
 */
public class BreakTimeAdapter extends SimpleAdapter {

    private BreakTimeActivity disturbActivity;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    boolean isEditMode;

    public BreakTimeAdapter(BreakTimeActivity disturbActivity) {
        super(disturbActivity);
        this.disturbActivity = disturbActivity;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_break_time) {
            ItemDisturbBinding binding = (ItemDisturbBinding) vh.getBinding();
            if (isEditMode) {
                binding.llyDelete.setVisibility(View.VISIBLE);
                binding.rlDisturb.setBackgroundResource(R.drawable.ripple_default);
            } else {
                binding.llyDelete.setVisibility(View.GONE);
                binding.rlDisturb.setBackgroundResource(R.drawable.bg_white);
            }
        }
        super.onBindViewBinding(vh, position);
    }
}
