package com.doctor.sun.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemOptionsBinding;
import com.doctor.sun.databinding.ItemQuestionBinding;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.activity.doctor.AddTemplateActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

import java.util.List;
import java.util.Objects;

/**
 * Created by lucas on 12/16/15.
 */
public class QuestionAdapter extends SimpleAdapter<Question, ItemQuestionBinding> {
    private boolean isEditMode;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public QuestionAdapter(AddTemplateActivity addTemplateActivity) {
        super(addTemplateActivity);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder vh, int position) {
        vh.getBinding().setVariable(BR.position, String.valueOf(position + 1));
        if (vh.getItemViewType() == R.layout.item_question) {
            ItemQuestionBinding binding = (ItemQuestionBinding) vh.getBinding();
            binding.llyOptions.removeAllViews();
            Question question = (Question) get(vh.getAdapterPosition());
            List<Options> optionsList = question.getOptions();
            for (Options options : optionsList) {
                ItemOptionsBinding optionsBinding = ItemOptionsBinding.inflate(LayoutInflater.from(getContext()), (ViewGroup) binding.getRoot(), false);
                binding.llyOptions.addView(optionsBinding.getRoot());
                if (!Objects.equals(options.getOptionContent(), "{fill}")) {
                    optionsBinding.tvOption.setText(options.getOptionType() + "." + options.getOptionContent());
                } else {
                    optionsBinding.tvOption.setText(options.getOptionType() + "." + "描述性回答");
                }
            }

            if (isEditMode) {
                binding.ivSelect.setVisibility(View.VISIBLE);
            } else {
                binding.ivSelect.setVisibility(View.GONE);
            }
        }
    }
}
