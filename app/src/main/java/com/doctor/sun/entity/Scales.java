package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.LeftDrawerFragmentActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.AnswerQuestionFragment;
import com.doctor.sun.ui.fragment.QuestionStatsFragment;
import com.doctor.sun.ui.fragment.ReadQTemplateFragment;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * Created by rick on 15/8/2016.
 */

public class Scales extends BaseItem {

    /**
     * scale_id : 14689161052OQbeOFbJq
     * scale_name : 焦虑
     */

    @JsonProperty("scale_id")
    public String scaleId;
    @JsonProperty("scale_name")
    public String scaleName;
    @Deprecated
    @JsonProperty("question_count")
    public String questionCount;
    @JsonProperty("refill_count")
    public int refillCount;


    public void readScalesQuestion(SortedListAdapter adapter, String scalesId, boolean isTemplates) {
        Context context = adapter.getContext();
        boolean isDone = adapter.getConfig(AdapterConfigKey.IS_DONE);
        Bundle args;
        if (isTemplates) {
            args = ReadQTemplateFragment.getArgs(scalesId, QuestionsPath.SCALES, "", isDone);
            args.putString(Constants.IS_TEMPLATE, "1");
        } else {
            args = ReadQuestionsFragment.getArgs(scalesId, QuestionsPath.SCALES, "", isDone);
        }
        Intent intent = SingleFragmentActivity.intentFor(context, scaleName, args);
        context.startActivity(intent);
    }

    public void editScalesQuestion(SortedListAdapter adapter, String scalesId) {
        Context context = adapter.getContext();
        boolean isDone = adapter.getConfig(AdapterConfigKey.IS_DONE);
        if (!isDone) {
            Bundle args = AnswerQuestionFragment.getArgs(scalesId, QuestionsPath.SCALES, "");
            Intent intent = SingleFragmentActivity.intentFor(context, scaleName, args);
            context.startActivity(intent);
        } else {
            Bundle args = ReadQuestionsFragment.getArgs(scalesId, QuestionsPath.SCALES, "", isDone);
            Intent intent = SingleFragmentActivity.intentFor(context, scaleName, args);
            context.startActivity(intent);
        }
    }


    public void editScalesQuestionWithResult(SortedListAdapter adapter, String scalesId) {
        Context context = adapter.getContext();
        boolean isDone = adapter.getConfig(AdapterConfigKey.IS_DONE);
        if (!isDone) {
            Bundle args = AnswerQuestionFragment.getArgs(scalesId, QuestionsPath.SCALES, "");
            Bundle drawerArgs = QuestionStatsFragment.getArgs(scalesId, "smartScaleResult");
            Intent intent = LeftDrawerFragmentActivity.intentFor(context, scaleName, args, drawerArgs);
            context.startActivity(intent);
        } else {
            Bundle args = ReadQuestionsFragment.getArgs(scalesId, QuestionsPath.SCALES, "", true);
            Bundle drawerArgs = QuestionStatsFragment.getArgs(scalesId, "smartScaleResult");
            Intent intent = LeftDrawerFragmentActivity.intentFor(context, scaleName, args, drawerArgs);
            context.startActivity(intent);
        }
    }

    public void addScaleToAppointment(String appointmentId) {
        HashMap<String, String> fieldMap = new HashMap<>();
        fieldMap.put("add_scale", scaleId);
        QuestionModule api = Api.of(QuestionModule.class);
        api.addQuestionToAppointment(appointmentId, fieldMap).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                setUserSelected(true);
            }
        });
    }

    public void showAddScaleDialog(Context context, final String appointmentId) {
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
                        addScaleToAppointment(appointmentId);
                    }
                }).show();
    }


    @Override
    public String getKey() {
        return scaleId;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_scales;
    }
}
