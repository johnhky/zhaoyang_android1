package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemSymptomBinding;
import com.doctor.sun.entity.Symptom;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by rick on 12/21/15.
 */
public class SuggestionAdapter extends SimpleAdapter {
    public SuggestionAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_symptom) {
           ItemSymptomBinding binding = (ItemSymptomBinding) vh.getBinding();
            binding.etOthers.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Symptom symptom = (Symptom) get(vh.getAdapterPosition());
                    symptom.setOthers(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}
