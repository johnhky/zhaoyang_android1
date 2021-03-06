package com.doctor.sun.entity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.constans.ClearRules;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.event.LoadDrugEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.Titration;
import com.doctor.sun.immutables.UploadDrugData;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.AdapterOps;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.NumericBooleanDeserializer;
import com.doctor.sun.util.NumericBooleanSerializer;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ItemAddPrescription2;
import com.doctor.sun.vm.ItemTextInput2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 28/7/2016.
 */

public class Options2 extends BaseItem {
    public static final String TAG = Options2.class.getSimpleName();

    private static final int FROM_API = 0;
    private static final int FROM_USER_INPUT = 1;
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
    public List<String> childOptions = new ArrayList<>();
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
    public String toString() {
        return "options:{optionContent:" + optionContent + "reply_content:" + inputContent + "}";
    }

    private BaseListAdapter<SortedItem, ViewDataBinding> adapter;

    @Bindable
    public String getInputContent() {
        return inputContent;
    }

    public void setInputContent(String inputContent) {
        this.inputContent = inputContent;
        notifyPropertyChanged(BR.inputContent);
    }

    @Override
    public int getLayoutId() {
        if (!isVisible()) {
            return R.layout.item_empty;
        }
        switch (questionType) {
            case QuestionType.drug:
                return R.layout.item_options_load_prescription;
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

    public void afterInputChanged(Editable newInputEditable, SortedListAdapter adapter) {
        String newInput = newInputEditable.toString();
        if (optionContent.equals(newInput)) {
            return;
        }
        boolean selected = !newInput.equals("");
        setSelectedWrap(selected, adapter);
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
        if (childOptions == null || index > childOptions.size() || index < 0) {
            return "";
        }
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
                    otherOptions.inputContent = "";
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
        if (otherOptions.clearRule == ClearRules.LEAVE_ME_ALONE_2) {
            return false;
        }
        if (clearRule == ClearRules.LEAVE_ME_ALONE_2) {
            return false;
        }

        if (clearRule == ClearRules.KILL_OTHERS_EXCEPT_TWO_1) {
            return true;
        }
        if (clearRule == ClearRules.KILL_OTHERS_EXCEPT_ZERO_OR_TWO_0) {
            return ClearRules.KILL_OTHERS_EXCEPT_ZERO_OR_TWO_0 != otherOptions.clearRule;
        }
        return false;
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
    public void loadPrescriptions(final SortedListAdapter adapter, final Context context) {
        if (resourseType == FROM_USER_INPUT) {
            /**
             *导入上面患者填写的用药
             */
            ItemAddPrescription2 fromItem = (ItemAddPrescription2) adapter.get(optionContent + QuestionType.drug);
            ItemAddPrescription2 toItem = (ItemAddPrescription2) adapter.get(questionId + QuestionType.drug);
            if (fromItem != null && toItem != null) {
                int adapterPosition = adapter.indexOfImpl(fromItem);
                int distance = adapter.inBetweenItemCount(adapterPosition, optionContent);
                if (distance > 1) {
                    for (int i = distance; i > 1; i--) {
                        int index = adapterPosition - i + 1;
                        try {
                            Prescription sortedItem = (Prescription) adapter.get(index);
                            toItem.addPrescription(sortedItem, adapter);
                        } catch (ClassCastException ignored) {
                            ignored.printStackTrace();
                        }
                    }
                } else {
                    EventHub.post(new LoadDrugEvent());
                }

            }
        } else if (resourseType == FROM_API) {
            /**
             * 导入api里面返回的用药
             */
            ToolModule toolModule = Api.of(ToolModule.class);
            toolModule.listOfDrugs(optionContent).enqueue(new SimpleCallback<List<UploadDrugData>>() {
                @Override
                protected void handleResponse(List<UploadDrugData> response) {
                    if (response == null || response.size() == 0) {
                        EventHub.post(new LoadDrugEvent());
                        return;
                    }
                    if (response.size() == 1) {
                        ItemAddPrescription2 item = (ItemAddPrescription2) adapter.get(questionId + QuestionType.drug);
                        for (int i = 0; i < response.size(); i++) {
                            for (Prescription prescription : response.get(i).getDrug_data()) {
                                prescription.setTitration(new ArrayList<Titration>());
                                prescription.setTake_medicine_days("");
                                item.addPrescription(prescription, adapter);
                            }
                        }
                    } else {
                        showDialog(context, response, adapter);
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

    public void showDialog(Context context, final List<UploadDrugData> data, final SortedListAdapter mAdapter) {
  /*      MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.stackingBehavior(StackingBehavior.ALWAYS)
                .btnStackedGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .title("请选择需要使用的处方")
                .neutralText("关闭")
                .adapter(adapter, new LinearLayoutManager(context))
                .show();*/
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.item_drug_popup, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        LinearLayout ll_add = (LinearLayout) view.findViewById(R.id.ll_add_drug);
        TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        for (int i = 0; i < data.size(); i++) {
            View ll_drug = LayoutInflater.from(context).inflate(R.layout.item_select_drug, null);
            LinearLayout ll_select = (LinearLayout) ll_drug.findViewById(R.id.ll_selectDrug);
            TextView tv_date = (TextView) ll_drug.findViewById(R.id.tv_date);
            TextView tv_name = (TextView) ll_drug.findViewById(R.id.tv_name);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(data.get(i).getCreated_at());
                tv_date.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_name.setText("【开药医生:" + data.get(i).getName() + "】");
            ll_add.addView(ll_drug);
            final int position = i;
            ll_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemAddPrescription2 item = (ItemAddPrescription2) mAdapter.get(questionId + QuestionType.drug);
                    UploadDrugData drug = data.get(position);
                    if (drug.getDrug_data().size() > 0) {
                        for (int i = 0; i < drug.getDrug_data().size(); i++) {
                            drug.getDrug_data().get(i).setTitration(new ArrayList<Titration>());
                            drug.getDrug_data().get(i).setTake_medicine_days("");
                            item.addPrescription(drug.getDrug_data().get(i), mAdapter);
                        }
                    }
                    dialog.dismiss();
                }

            });
        }
    }

//    public BaseListAdapter<SortedItem, ViewDataBinding> getAdapter() {
//        return adapter;
//    }
//
//    public void setAdapter(BaseListAdapter<SortedItem, ViewDataBinding> adapter) {
//        this.adapter = adapter;
//    }

    /*public BaseListAdapter<SortedItem, ViewDataBinding> DrugAdapter(List<UploadDrugData> datas, final SortedListAdapter mAdapter) {

        List<SortedItem> result = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            final UploadDrugData data = datas.get(i);
            ItemTextInput2 drug = new ItemTextInput2(R.layout.item_select_drug, "", "");
            drug.setPosition(result.size() + i);
            drug.setItemId("drug" + i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = simpleDateFormat.parse(data.getCreated_at());
                drug.setTitle(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            drug.setResult(data.getName());
            drug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemAddPrescription2 item = (ItemAddPrescription2) mAdapter.get(questionId + QuestionType.drug);
                    if (data.getDrug_data().size() > 0) {
                        for (int i = 0; i < data.getDrug_data().size(); i++) {
                            data.getDrug_data().get(i).setTitration(new ArrayList<Titration>());
                            data.getDrug_data().get(i).setTake_medicine_days("");
                            item.addPrescription(data.getDrug_data().get(i), mAdapter);
                        }
                    }
                }
            });
            result.add(drug);
        }
        SortedListAdapter<ViewDataBinding> adapter = new SortedListAdapter<>();
        adapter.insertAll(result);
        return adapter;
    }
*/

}
