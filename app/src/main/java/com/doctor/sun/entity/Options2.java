package com.doctor.sun.entity;

import android.content.Context;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.NumericBooleanDeserializer;
import com.doctor.sun.util.NumericBooleanSerializer;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rick on 28/7/2016.
 */

public class Options2 extends BaseItem {
    public static final String TAG = Options2.class.getSimpleName();

    /**
     * base_option_content : 保存密码
     * base_option_id : 1468916105WaJyrupXco
     * base_option_type : A
     * clear_rule : 0
     * reply_content :
     * selected : 0
     */
    @JsonIgnore
    public String questionId;
    @JsonIgnore
    public String questionType;
    @JsonIgnore
    public String questionContent;

    @JsonSerialize(using = NumericBooleanSerializer.class)
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("selected")
    private Boolean selected;
    @JsonProperty("clear_rule")
    public int clearRule;
    @JsonProperty("base_option_id")
    public String optionId;
    @JsonProperty("base_option_type")
    public String optionType;
    @JsonProperty("base_option_content")
    public String optionContent = "";
    @JsonProperty("content_head")
    public String contentHead = "";
    @JsonProperty("option_input_hint")
    public String optionInputHint = "";
    @JsonProperty("option_input_type")
    public int optionInputType = InputType.TYPE_CLASS_TEXT;
    @JsonProperty("option_input_length")
    public int optionInputLength = 0;
    @JsonProperty("content_tail")
    public String contentTail = "";
    @JsonProperty("base_option_array")
    public List<String> childOptions;
    @JsonProperty("reply_index")
    public int selectedIndex = -1;
    @JsonProperty("reply_object")
    public Map<String, String> selectedOption;
    @JsonProperty("reply_content")
    public String inputContent;

    @Override
    public int getLayoutId() {
        switch (questionType) {
            case QuestionType.sel:
                return R.layout.new_item_options_dialog;
            case QuestionType.rectangle:
                return R.layout.new_item_options_rect;
            default:
                return R.layout.new_item_options;
        }
    }

    @Override
    public String getKey() {
        return optionId;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (selected) {
            if (getLayoutId() == R.layout.new_item_options_dialog) {
                if (selectedIndex > 0) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("option_id", optionId);
                    result.put("reply_content", selectedIndex);
                    return result;
                } else {
                    return null;
                }
            } else {
                HashMap<String, Object> result = new HashMap<>();
                result.put("option_id", optionId);
                result.put("reply_content", inputContent);
                return result;
            }

        } else {
            return null;
        }
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getOption(int index) {
        try {
            return childOptions.get(index);
        } catch (Exception e) {
            return childOptions.get(0);
        }
    }

    public void clear(SortedListAdapter adapter) {
        notifyChange();
        if (!selected) {
            adapter.update(adapter.get(questionId));
            return;
        }

        Questions2 sortedItem = (Questions2) adapter.get(questionId);
        for (Options2 options2 : sortedItem.option) {
            if (!options2.optionId.equals(this.optionId)) {
                if (options2.clearRule != clearRule) {
                    options2.selected = Boolean.FALSE;
                    adapter.insert(options2);
                }
            }
        }
        adapter.insert(sortedItem);
    }

    public void showDialog(Context context) {
        if (childOptions == null || childOptions.isEmpty()) {
            return;
        }
        new MaterialDialog.Builder(context)
                .title(questionContent)
                .items(childOptions)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        selected = Boolean.TRUE;
                        selectedIndex = which;
                        notifyChange();
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    @Override
    public int getSpan() {
        if (questionType.equals(QuestionType.rectangle)) {
            if (optionContent == null) {
                return 0;
            }
            int i = optionContent.length();
            return Math.min(12, Math.max(2, i));
        }
        return super.getSpan();
    }
}
