package com.doctor.sun.entity.handler;

import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.text.InputType;
import android.view.View;

import com.doctor.sun.entity.Options;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 30/3/2016.
 */
public class OptionsHandler {

    public static final String BREAK_POINT = ". ";
    public static final String INDICATOR = "{fill}";

    public boolean isFill(String content) {
        return content.contains("{fill}");
    }

    public String hint(Options data) {
        String optionContent = data.getOptionContent();
        if (optionContent.equals(INDICATOR)) {
            return "其他";
        } else if (optionContent.contains(INDICATOR)) {
            return "多少";
        } else {
            return "";
        }
    }


    public int inputType(Options data) {
        String optionContent = data.getOptionContent();
        if (optionContent.equals(INDICATOR)) {
            return InputType.TYPE_CLASS_TEXT;
        } else if (optionContent.contains(INDICATOR)) {
            return InputType.TYPE_CLASS_NUMBER;
        } else {
            return InputType.TYPE_CLASS_TEXT;
        }
    }

    public String content(Options data) {
        String optionContent = data.getOptionContent();
        if (optionContent.equals(INDICATOR)) {
            return data.getOptionType() + BREAK_POINT;
        }
        int end = optionContent.indexOf(INDICATOR);
        if (end > 0) {
            return data.getOptionType() + BREAK_POINT + optionContent.substring(0, end);
        } else {
            return data.getOptionType() + BREAK_POINT + optionContent;
        }
    }

    public String remainingContent(Options data) {
        String optionContent = data.getOptionContent();
        int end = optionContent.indexOf(INDICATOR);
        if (end > 0) {
            return optionContent.substring(end + 6, optionContent.length());
        } else {
            return "";
        }
    }

    public TextViewBindingAdapter.AfterTextChanged contentChanged(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new TextViewBindingAdapter.AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                int position = vh.getAdapterPosition();

                Options options = (Options) adapter.get(position);
                options.setOptionInput(s.toString());

                adapter.set(position, options);
            }
        };
    }

}
