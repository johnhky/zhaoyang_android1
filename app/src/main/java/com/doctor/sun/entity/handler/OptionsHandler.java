package com.doctor.sun.entity.handler;

import android.databinding.adapters.TextViewBindingAdapter;
import android.text.Editable;
import android.text.InputType;

import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

import java.util.HashMap;

/**
 * Created by rick on 30/3/2016.
 */
public class OptionsHandler {
    public static final String TAG = OptionsHandler.class.getSimpleName();

    public static final String BREAK_POINT = ".   ";
    public static final String INDICATOR = "{fill}";

    public boolean isFill(String content) {
        return content.contains("{fill}");
    }

    public String hintV2(Options data) {
        String optionContent = data.getOptionContent();
        if (optionContent.contains(INDICATOR)) {
            int end = optionContent.indexOf(INDICATOR);
            if (end > 0) {
                return optionContent.substring(0, end);
            } else {
                return optionContent;
            }
        } else {
            return "";
        }
    }


    public int inputTypeV2(Options data) {
        String optionContent = data.getOptionContent();
        if (optionContent.equals(INDICATOR)) {
            return InputType.TYPE_CLASS_TEXT;
        } else if (optionContent.contains(INDICATOR)) {
            return InputType.TYPE_CLASS_TEXT;
        } else {
            return InputType.TYPE_CLASS_TEXT;
        }
    }

    public String contentV2(Options data) {
        String optionContent = data.getOptionContent();

        if (optionContent.contains(INDICATOR)) {
            return data.getOptionType() + BREAK_POINT;
        } else {
            return data.getOptionType() + BREAK_POINT + optionContent;
        }
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

    public String optionInput(final BaseAdapter adapter, final BaseViewHolder vh) {
        int adapterPosition = vh.getAdapterPosition();
        Options options = (Options) adapter.get(adapterPosition);
        if (options.getOptionInput() == null) {
            int parentPosition = options.getParentPosition();
            Answer answer = getParent(adapter, parentPosition);
            options.setOptionInput(answer.getSelectedOptions().get(options.getOptionType()));
        }
        return options.getOptionInput();
    }

    public TextViewBindingAdapter.AfterTextChanged contentChanged(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new TextViewBindingAdapter.AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                int childAdapterPosition = vh.getAdapterPosition();
                if (childAdapterPosition < 0 || childAdapterPosition > adapter.size()) {
                    return;
                }
                Options options = (Options) adapter.get(childAdapterPosition);
                options.setOptionInput(s.toString());
                int parentPosition = options.getParentPosition();
                int childDataPosition = childAdapterPosition - parentPosition;

                Answer parent = getParent(adapter, parentPosition);
                parent.getQuestion().getOptions().set(childDataPosition - 1, options);

                if (parent.getSelectedOptions().containsKey(options.getOptionType())) {
                    parent.getSelectedOptions().put(options.getOptionType(), options.getOptionInput());
                }
                adapter.set(childAdapterPosition, options);
                adapter.set(options.getParentPosition(), parent);
            }
        };
    }

    public Answer getParent(BaseAdapter adapter, int position) {
        return (Answer) adapter.get(position);
    }

    //lambda listener binding, method return type must match the return type of the listener
    public boolean selectWrapper(BaseAdapter adapter, BaseViewHolder viewHolder) {
        select(adapter, viewHolder);
        return false;
    }

    public void select(final BaseAdapter adapter, final BaseViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();

        Options options = (Options) adapter.get(adapterPosition);

        int parentPosition = options.getParentPosition();
        Answer answer = getParent(adapter, parentPosition);

        HashMap<String, String> selectedOptions = answer.getSelectedOptions();

        switch (answer.getQuestion().getQuestionType()) {
            case Question.TYPE_SEL:
            case Question.TYPE_RADIO: {
                HashMap<String, String> stringStringHashMap = selectThisClearOthers(options, selectedOptions);
                answer.setSelectedOptions(stringStringHashMap);
                notifyDataSetChange(parentPosition, answer, adapter);
                break;
            }
            case Question.TYPE_CHECKBOX: {
                handleCheckBox(options, parentPosition, answer, selectedOptions, adapter);
                break;
            }
        }
    }

    public void handleCheckBox(Options options, int parentPosition, Answer answer, HashMap<String, String> selectedOptions, BaseAdapter adapter) {
        String clearOption = answer.getQuestion().getClearOption();
        if (clearOption.equals(options.getOptionType())) {
            //选择了清除选项
            selectThisClearOthers(options, selectedOptions);
            notifyDataSetChange(parentPosition, answer, adapter);
        } else {
            //一般的checkbox
            toggleSelection(options, answer, selectedOptions, clearOption);
            notifyDataSetChange(parentPosition, answer, adapter);
        }
    }

    public void toggleSelection(Options options, Answer answer, HashMap<String, String> selectedOptions, String clearOption) {
        String s = selectedOptions.get(options.getOptionType());

        if (s == null) {
            if (isFill(options.getOptionContent())) {
                selectedOptions.put(options.getOptionType(), options.getOptionInput());
            } else {
                selectedOptions.put(options.getOptionType(), options.getOptionContent());
            }
        } else {
            selectedOptions.remove(options.getOptionType());
        }
        selectedOptions.remove(clearOption);
        answer.setAnswerContent(selectedOptions.values());
        answer.setAnswerType(selectedOptions.keySet());
    }

    public void notifyDataSetChange(int parentPosition, Answer answer, BaseAdapter adapter) {
        adapter.set(parentPosition, answer);
        adapter.notifyDataSetChanged();
    }

    public HashMap<String, String> selectThisClearOthers(Options options, HashMap<String, String> selectedOptions) {
        selectedOptions.clear();
        if (isFill(options.getOptionContent())) {
            selectedOptions.put(options.getOptionType(), options.getOptionInput());
        } else {
            selectedOptions.put(options.getOptionType(), options.getOptionContent());
        }
        return selectedOptions;
    }

    public boolean isSelected(final BaseAdapter adapter, final BaseViewHolder viewHolder) {
        Options options = (Options) adapter.get(viewHolder.getAdapterPosition());
        Answer answer = getParent(adapter, options.getParentPosition());
        return answer.getSelectedOptions().containsKey(options.getOptionType());
    }
}
