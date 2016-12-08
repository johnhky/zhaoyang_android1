package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.constans.QuestionsType;
import com.doctor.sun.event.ModifyQuestionsEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.ReadQTemplateFragment;
import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 5/9/2016.
 */

public class QTemplate2 extends BaseItem {

    /**
     * custom_amount : 0
     * default_type : 0
     * independent_amount : 3
     * scale_amount : 0
     * template_id : 14683081932d4Jz0ikQ3
     * template_name : 东方美方面
     */

    @JsonProperty("default_type")
    public String defaultType;
    @JsonProperty("template_id")
    public String templateId;
    @JsonProperty("template_name")
    public String templateName;
    @JsonProperty("amount")
    private int questionsCount;

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }

    public int getQuestionCount() {
        return questionsCount;
    }

    public void readQuestions(Context context) {
        Bundle bundle = ReadQTemplateFragment.getArgs(
                templateId, QuestionsPath.TEMPLATE,
                QuestionsType.DOCTOR_R_PATIENT_QUESTIONS, true);
        Intent intent = SingleFragmentActivity.intentFor(context, templateName, bundle);
        context.startActivity(intent);
    }

    public void addTemplateToAppointment(final String appointmentId) {
        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("add_template", templateId);
        QuestionModule api = Api.of(QuestionModule.class);
        api.addQuestionToAppointment(appointmentId, fieldMap).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                setUserSelected(true);
                EventHub.post(new ModifyQuestionsEvent(appointmentId));
            }
        });
    }

    public void showAddTemplateDialog(Context context, final String appointmentId) {
        if (isUserSelected()) {
            Toast.makeText(context, "量表已经添加", Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(context)
                .content("是否确认添加")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addTemplateToAppointment(appointmentId);
                    }
                }).show();
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_template2;
    }
}
