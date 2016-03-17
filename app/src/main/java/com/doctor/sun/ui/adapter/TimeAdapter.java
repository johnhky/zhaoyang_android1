package com.doctor.sun.ui.adapter;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemTimeBinding;
import com.doctor.sun.ui.activity.doctor.TimeActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by lucas on 12/15/15.
 */
public class TimeAdapter extends SimpleAdapter {

    private TimeActivity timeActivity;
    private boolean isEditMode;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public TimeAdapter(TimeActivity timeActivity) {
        super(timeActivity);
        this.timeActivity = timeActivity;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_time) {
            ItemTimeBinding binding = (ItemTimeBinding) vh.getBinding();
            if (isEditMode) {
                binding.llyDelete.setVisibility(View.VISIBLE);
            } else {
                binding.llyDelete.setVisibility(View.INVISIBLE);
            }
        }
        super.onBindViewBinding(vh, position);
    }
}
