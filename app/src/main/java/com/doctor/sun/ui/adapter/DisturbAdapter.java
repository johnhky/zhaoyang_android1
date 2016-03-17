package com.doctor.sun.ui.adapter;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemDisturbBinding;
import com.doctor.sun.ui.activity.doctor.DisturbActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by lucas on 12/15/15.
 */
public class DisturbAdapter extends SimpleAdapter {

    private DisturbActivity disturbActivity;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    boolean isEditMode;

    public DisturbAdapter(DisturbActivity disturbActivity) {
        super(disturbActivity);
        this.disturbActivity = disturbActivity;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_disturb) {
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
