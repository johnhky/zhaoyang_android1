package com.doctor.sun.entity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemCustomQuestionLoader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rick on 28/7/2016.
 */

public class Questions2 extends BaseItem {
    public static final String TAG = Questions2.class.getSimpleName();


    /**
     * option : [{"base_option_content":"保存密码","base_option_id":"1468916105WaJyrupXco","base_option_type":"A","clear_rule":0,"reply_content":"","selected":0}]
     * base_question_content : 要让360浏览器保存你的密码吗？
     * base_question_id : 1468916105DQZJhKSiga
     * base_question_type : radio
     * fill_content :
     * old_question_id : 1468305806tx8YKhpITn
     * refill : 0
     */

    @JsonIgnore
    public int answerCount = 0;
    @JsonIgnore
    public int questionIndex;

    @JsonProperty("question_answer_id")
    public String answerId;
    @JsonProperty("question_rule_id")
    public String questionId;
    @JsonProperty("question_type")
    public String questionType = "";
    @JsonProperty("question_content")
    public String questionContent;
    @JsonProperty("fill_content")
    public String fillContent;
    @JsonProperty("refill")
    public int refill;
    @JsonProperty("extend_type")
    public int extendType = 0;

    @JsonProperty("option")
    public List<Options2> option;

    //不懂找颜升
    @JsonProperty("array_content")
    public List<Map<String, String>> arrayContent;

    @JsonProperty("or_enable_rule")
    public List<String> orEnableRule;
    @JsonProperty("or_disable_rule")
    public List<String> orDisableRule;


    @JsonProperty("expand_button")
    public QuVisibilityManager questionsButton;

    public String positionString() {
        return String.valueOf(questionIndex + 1);
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_question;
    }


    @Override
    public String getKey() {
        if (Strings.isNullOrEmpty(questionId)) {
            if (Strings.isNullOrEmpty(answerId)) {
                questionId = UUID.randomUUID().toString();
                return questionId;
            } else {
                return answerId;
            }
        }
        return questionId;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }

    public String getOptionContent(int position) {
        if (option == null || option.size() < position) {
            return "";
        }
        return option.get(position).optionContent;
    }

    public String getOptionID(int position) {
        if (option == null || option.size() < position) {
            return "";
        }
        return option.get(position).optionAnswerId;
    }

    public boolean isAnswered(SortedListAdapter adapter, BaseViewHolder vh) {
        if (option != null) {
            for (Options2 options2 : option) {
                if (options2.getSelected()) {
                    return true;
                }
            }
        }
        if (answerCount > 0) {
            return true;
        }
        int i = adapter.inBetweenItemCount(vh.getAdapterPosition(), questionId + questionType);
        if (Math.abs(i) > 1) {
            return true;
        }

        if (questionType.equals(QuestionType.asel) && arrayContent != null && arrayContent.size() > 0) {
            return true;
        }
        return false;

    }

    public boolean isEnabled(SortedListAdapter adapter, boolean isEnabledLastTime) {

        boolean result = isEnabledLastTime;
        int selectedOptionsCount = 0;

        if (orDisableRule != null && !orDisableRule.isEmpty()) {
            for (String s : orDisableRule) {
                boolean isSelected = isSelected(s, adapter);
                if (isSelected) {
                    result = false;
                    selectedOptionsCount += 1;
                    break;
                }
            }
        }

        if (orEnableRule != null && !orEnableRule.isEmpty()) {
            for (String s : orEnableRule) {
                boolean isSelected = isSelected(s, adapter);
                if (isSelected) {
                    result = true;
                    selectedOptionsCount += 1;
                    break;
                }
            }
        }

        // 有规则信息的时候,假如没有任何规则适用,那就默认disable
        if (hasRulesInfo() && selectedOptionsCount == 0) {
            result = true;
        }

        // enabled状态发生改变,通知子项目改变状态
        if (isEnabledLastTime != result) {
            setEnabledDontNotify(result);
            enableOrDisableChild(result);
            enableOrDisableCustomChild(adapter, result);
        }
        return result;
    }


    public boolean isSelected(String key, SortedListAdapter adapter) {
        Options2 options2 = (Options2) adapter.get(key);
        return options2 != null && options2.getSelected();
    }

    //TODO:
    private void enableOrDisableChild(boolean enabled) {
        if (option != null) {
            for (Options2 myOptions : option) {
                if (myOptions != null) {
                    myOptions.setEnabled(enabled);
                }
            }
        }
    }

    private void enableOrDisableCustomChild(SortedListAdapter adapter, boolean result) {
        SortedItem item = adapter.get(getKey() + questionType);
        if (item != null) {
            BaseItem baseItem = (BaseItem) item;
            baseItem.setEnabled(result);
        }
    }

    private boolean hasRulesInfo() {
        return orDisableRule != null || orEnableRule != null;
    }

    private void addQuestionToAppointment(String appointmentId) {
        HashMap<String, String> fieldMap = new HashMap<>();
        if (getPosition() > ItemCustomQuestionLoader.FIRST_ITEM_POSITION_PADDING * QuestionsModel.PADDING) {
            fieldMap.put("add_template_question", getKey());
        } else {
            fieldMap.put("add_base_question", getKey());
        }
        QuestionModule api = Api.of(QuestionModule.class);
        api.addQuestionToAppointment(appointmentId, fieldMap).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                setUserSelected(true);
            }
        });
    }

    public void showAddQuestionsDialog(Context context, final String appointmentId) {
        if (isUserSelected()) {
            Toast.makeText(context, "问题已经添加", Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(context)
                .content("是否确认添加")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addQuestionToAppointment(appointmentId);
                    }
                }).show();
    }
}
