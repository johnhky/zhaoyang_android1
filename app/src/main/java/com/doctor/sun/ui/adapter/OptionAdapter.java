package com.doctor.sun.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemOptionAnswerBinding;
import com.doctor.sun.databinding.ItemOptionSelectBinding;
import com.doctor.sun.entity.ItemTextInput;
import com.doctor.sun.ui.activity.doctor.AddQuestionActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.widget.SideSelector;

/**
 * Created by lucas on 12/28/15.
 */
public class OptionAdapter extends SimpleAdapter {

    public OptionAdapter(AddQuestionActivity addQuestionActivity) {
        super(addQuestionActivity);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, int position) {
        if (vh.getItemViewType() == R.layout.item_option_select) {
            ItemOptionSelectBinding binding = (ItemOptionSelectBinding) vh.getBinding();
            binding.tvSelect.setText(SideSelector.ALPHABET[vh.getAdapterPosition()] + ".");
            ItemTextInput itemTextInput = (ItemTextInput)get(vh.getAdapterPosition());
            itemTextInput.setTitle(String.valueOf(SideSelector.ALPHABET[vh.getAdapterPosition()]));

            binding.etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ItemTextInput itemTextInput = (ItemTextInput) get(vh.getAdapterPosition());
                    itemTextInput.setInput(s.toString());
                }
            });
        }

        if (vh.getItemViewType() == R.layout.item_option_answer) {
            ItemOptionAnswerBinding binding = (ItemOptionAnswerBinding) vh.getBinding();
            binding.tvAnswer.setText(SideSelector.ALPHABET[vh.getAdapterPosition()] + ".");
            ItemTextInput itemTextInput = (ItemTextInput)get(vh.getAdapterPosition());
            itemTextInput.setTitle(String.valueOf(SideSelector.ALPHABET[vh.getAdapterPosition()]));
        }
    }
}
