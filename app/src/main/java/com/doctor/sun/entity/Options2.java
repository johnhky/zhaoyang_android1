package com.doctor.sun.entity;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.ImmutablePrescription;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.AdapterOps;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.NumericBooleanDeserializer;
import com.doctor.sun.util.NumericBooleanSerializer;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemAddPrescription2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rick on 28/7/2016.
 */

public class Options2 extends BaseItem {
    public static final String TAG = Options2.class.getSimpleName();

    private static final int FORM_API = 0;
    private static final int FORM_USER_INPUT = 1;

    /**
     * option_content : 保存密码
     * option_id : 1468916105WaJyrupXco
     * option_type : A
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
    private Boolean selected = Boolean.FALSE;
    @JsonProperty("clear_rule")
    public int clearRule;
    @JsonProperty("option_answer_id")
    public String optionAnswerId;
    @JsonProperty("option_rule_id")
    public String optionRuleId;
    @JsonProperty("option_type")
    public String optionType;
    @JsonProperty("option_content")
    public String optionContent = "";
    @JsonProperty("content_head")
    public String contentHead = "";
    @JsonProperty("option_input_hint")
    public String optionInputHint = "";
    @JsonProperty("option_input_type")
    public int optionInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
    @JsonProperty("option_input_length")
    public int optionInputLength = 0;
    @JsonProperty("content_tail")
    public String contentTail = "";
    @JsonProperty("option_array")
    public List<String> childOptions;
    @JsonProperty("reply_index")
    public int selectedIndex = -1;

    @JsonProperty("resourse_type")
    public int resourseType;

    @JsonProperty("reply_object")
    public Map<String, String> selectedOption;
    @JsonProperty("reply_content")
    public String inputContent;

    @JsonProperty("option_or_enable")
    public List<String> optionOrEnable;
    @JsonProperty("option_or_disable")
    public List<String> optionOrDisable;

    @Override
    public int getLayoutId() {
        if (!isVisible()) {
            return R.layout.item_empty;
        }
        switch (questionType) {
            case QuestionType.drug:
                return R.layout.item_load_prescription;
            case QuestionType.sel:
                return R.layout.item_options_dialog;
            case QuestionType.rectangle:
                if (!Strings.isNullOrEmpty(optionInputHint)) {
                    return R.layout.item_options_rect_input;
                }
                return R.layout.item_options_rect;
            default:
                return R.layout.item_options;
        }
    }

    @Override
    public int getSpan() {
        if (questionType.equals(QuestionType.rectangle)) {
            if (!Strings.isNullOrEmpty(optionInputHint)) {
                return 12;
            }

            if (Strings.isNullOrEmpty(optionContent)) {
                return 0;
            }
            int i = optionContent.length();
            if (i < 6) {
                i = i - 1;
            }

            return Math.min(12, Math.max(2, i));
        }
        return super.getSpan();
    }

    @Override
    public String getKey() {
        if (Strings.isNullOrEmpty(optionRuleId)) {
            if (Strings.isNullOrEmpty(optionAnswerId)) {
                optionRuleId = UUID.randomUUID().toString();
                return optionRuleId;
            } else {
                return optionAnswerId;
            }
        } else {
            return optionRuleId;
        }
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (selected && isEnabled()) {
            if (getLayoutId() == R.layout.item_options_dialog) {
                if (selectedIndex > 0) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("option_id", optionAnswerId);
                    result.put("reply_content", selectedIndex);
                    return result;
                } else {
                    return null;
                }
            } else {
                HashMap<String, Object> result = new HashMap<>();
                result.put("option_id", optionAnswerId);
                result.put("reply_content", inputContent);
                return result;
            }

        } else {
            return null;
        }
    }

    public void afterInputChanged(Editable newInputEditable) {
        String newInput = newInputEditable.toString();
        if (optionContent.equals(newInput)) {
            return;
        }
        boolean selected = !newInput.equals("");
        setSelectedWrap(selected);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        if (!selected.equals(this.selected)) {
            this.selected = selected;
            notifyChange();
        }
    }

    public void setSelectedWrap(Boolean selected) {
        if (!selected.equals(this.selected)) {
            this.selected = selected;
            notifyChange();
        }
    }

    public void setSelectedWrap(Boolean selected, SortedListAdapter adapter) {
        if (!selected.equals(this.selected)) {
            this.selected = selected;
            clear(adapter, getChangedItem(adapter));
        }
    }

    private ArrayList<SortedItem> getChangedItem(SortedListAdapter adapter) {
        ArrayList<SortedItem> result = new ArrayList<>();
        if (optionOrEnable != null) {
            for (String s : optionOrEnable) {
                SortedItem e = adapter.get(s);
                if (e != null) {
                    result.add(e);
                }
            }
        }
        if (optionOrDisable != null) {
            for (String s : optionOrDisable) {
                SortedItem e = adapter.get(s);
                if (e != null) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public String getOption(int index) {
        try {
            return childOptions.get(index);
        } catch (Exception e) {
            return childOptions.get(0);
        }
    }

    public void clear(SortedListAdapter adapter) {
        clear(adapter, null);
    }

    /**
     * 根据 选择状态 和 refill 类型 清除其它选项
     *
     * @param adapter
     */
    public void clear(SortedListAdapter adapter, ArrayList<SortedItem> changedItem) {
        if (changedItem == null) {
            changedItem = new ArrayList<>();
        }
        changedItem.add(this);
        if (!getSelected()) {
            Questions2 questions = (Questions2) adapter.get(questionId);
            questions.refill = 0;
            changedItem.add(questions);
            for (SortedItem item : changedItem) {
                BaseItem temp = (BaseItem) item;
                temp.notifyChange();
            }
            return;
        }

        Questions2 sortedItem = (Questions2) adapter.get(questionId);
        for (Options2 otherOptions : sortedItem.option) {
            if (!otherOptions.getKey().equals(this.getKey())) {
                if (shouldClearThat(otherOptions)) {
                    otherOptions.setSelectedWrap(Boolean.FALSE, adapter);
                    changedItem.add(otherOptions);
                }
            }
        }
        sortedItem.refill = 0;
        changedItem.add(sortedItem);
        for (SortedItem item : changedItem) {
            BaseItem temp = (BaseItem) item;
            temp.notifyChange();
        }
    }

    private boolean shouldClearThat(Options2 otherOptions) {
        if (QuestionType.radio.equals(questionType)) {
            return true;
        }
        return otherOptions.clearRule != clearRule;
    }

    /**
     * 单选题点击事件
     *
     * @param context
     * @param adapter
     */
    public void showDialog(Context context, final AdapterOps adapter) {
        if (childOptions == null || childOptions.isEmpty()) {
            return;
        }
        new MaterialDialog.Builder(context)
                .title(questionContent)
                .items(childOptions)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        setSelected(Boolean.TRUE);
                        selectedIndex = which;
                        Questions2 sortedItem = (Questions2) adapter.get(questionId);
                        sortedItem.notifyChange();
                        notifyChange();
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }


    /**
     * 加载用药(只有drug类型的问题,才回用到)
     *
     * @param adapter
     */
    public void loadPrescriptions(final SortedListAdapter adapter) {
        if (resourseType == FORM_USER_INPUT) {
            ItemAddPrescription2 fromItem = (ItemAddPrescription2) adapter.get(optionContent + QuestionType.drug);
            ItemAddPrescription2 toItem = (ItemAddPrescription2) adapter.get(questionId + QuestionType.drug);
            if (fromItem != null && toItem != null) {
                int adapterPosition = adapter.indexOfImpl(fromItem);
                int distance = adapter.inBetweenItemCount(adapterPosition, optionContent);
                if (distance > 0) {
                    for (int i = distance; i > 1; i--) {
                        int index = adapterPosition - i + 1;
                        try {
                            Prescription sortedItem = (Prescription) adapter.get(index);
                            toItem.addPrescription(ImmutablePrescription.copyOf(sortedItem), adapter);
                        } catch (ClassCastException ignored) {
                            ignored.printStackTrace();
                        }
                    }
                }

            }
        } else {
            ToolModule toolModule = Api.of(ToolModule.class);
            toolModule.listOfDrugs(optionContent).enqueue(new SimpleCallback<List<Prescription>>() {
                @Override
                protected void handleResponse(List<Prescription> response) {
                    ItemAddPrescription2 item = (ItemAddPrescription2) adapter.get(questionId + QuestionType.drug);
                    for (Prescription prescription : response) {
                        item.addPrescription(prescription, adapter);
                    }
                }
            });
        }
    }


    /**
     * 输入框要输入的内容的类型
     *
     * @return
     */
    public int inputType() {
        switch (optionInputType) {
            case 0:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
            case 1:
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
            case 2:
                return InputType.TYPE_CLASS_NUMBER;
            default:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
        }
    }

    public boolean show() {
        return !Strings.isNullOrEmpty(optionInputHint) || !Strings.isNullOrEmpty(contentHead) || !Strings.isNullOrEmpty(contentTail);
    }
}
