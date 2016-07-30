package com.doctor.sun.entity;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.NumericBooleanDeserializer;
import com.doctor.sun.util.NumericBooleanSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

/**
 * Created by rick on 28/7/2016.
 */

public class Options2 extends BaseObservable implements SortedItem {
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
    public int position;
    @JsonIgnore
    public String questionId;
    @JsonIgnore
    public String questionType;

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
    public String optionContent;
    @JsonProperty("base_option_array")
    public List<OptionsPair> childOptions;
    @Bindable
    @JsonProperty("reply_object")
    public Map<String, String> selectedOption;
    @JsonProperty("reply_content")
    public String inputContent;

    @Override
    public int getLayoutId() {
        if (questionType != null && questionType.equals("sel")) {
            return R.layout.new_item_options_dialog;
        }
        return R.layout.new_item_options;
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return optionId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public OptionsPair getOption(String index) {
        try {
            return childOptions.get(Integer.parseInt(index));
        } catch (Exception e) {
            return childOptions.get(0);
        }
    }

    public void clear(SortedListAdapter adapter) {
        if (clearRule == 0) {
            return;
        }

        if (!selected) {
            return;
        }

        Questions2 sortedItem = (Questions2) adapter.get(questionId);
        for (Options2 options2 : sortedItem.option) {
            if (options2.optionId.equals(this.optionId)) {
                continue;
            } else {
                options2.selected = Boolean.FALSE;
                adapter.insert(options2);
            }
        }
    }

    public void showDialog(Context context) {
        if (childOptions == null || childOptions.isEmpty()) {
            return;
        }
        Integer selectedIndex;
        try {
            selectedIndex = Integer.valueOf(inputContent);
        } catch (Exception e) {
            selectedIndex = 0;
        }
        new MaterialDialog.Builder(context)
                .title("")
                .items(childOptions)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        inputContent = String.valueOf(which);
                        notifyChange();
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }


}
