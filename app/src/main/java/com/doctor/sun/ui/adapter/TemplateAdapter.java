package com.doctor.sun.ui.adapter;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemTemplateBinding;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.ui.activity.doctor.TemplateActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by rick on 12/15/15.
 */
public class TemplateAdapter extends SimpleAdapter {

    private boolean isEditMode;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public TemplateAdapter(TemplateActivity templateActivity) {
        super(templateActivity);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder vh, final int position) {

        if (vh.getItemViewType() == R.layout.item_template) {
            ItemTemplateBinding binding = (ItemTemplateBinding) vh.getBinding();
            QTemplate o = (QTemplate) get(position);
            if (isEditMode) {
                binding.llyDelete.setVisibility(View.VISIBLE);
                binding.llyDefault.setVisibility(View.GONE);
            } else {
                binding.llyDelete.setVisibility(View.GONE);
                switch (o.getIsDefault()){
                    case 1:binding.llyDefault.setVisibility(View.VISIBLE);break;
                    case 2:binding.llyDefault.setVisibility(View.GONE);break;
                    default:binding.llyDefault.setVisibility(View.GONE);break;
                }
            }
        }
    }
}
