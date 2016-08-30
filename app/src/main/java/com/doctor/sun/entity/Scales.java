package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.ui.activity.patient.EditQuestionActivity;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ReadQuestionFragment;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

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


    public void readScalesQuestion(SortedListAdapter adapter, String scalesId) {
        Context context = adapter.getContext();
        Intent i = ReadQuestionFragment.intentFor(context, scalesId, QuestionsPath.SCALES, "", adapter.getConfig(AdapterConfigKey.IS_DONE));
        context.startActivity(i);
    }

    public void editScalesQuestion(SortedListAdapter adapter, String scalesId) {
        Context context = adapter.getContext();
        Intent i = EditQuestionActivity.intentFor(context, scalesId, QuestionsPath.SCALES);
        context.startActivity(i);
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_scales;
    }
}
