package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.databinding.ItemAnswerBinding;
import com.doctor.sun.databinding.ItemPrescription2Binding;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Prescription;

/**
 * 填写问卷 编辑答案 adapter
 * Created by Lynn on 1/19/16.
 */
public class AnswerDetailAdapter extends AnswerModifyAdapter {

    public AnswerDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getPrescriptionView(ItemAnswerBinding binding, Answer answer, Prescription data) {
        final ItemPrescription2Binding prescriptionBinding = ItemPrescription2Binding.inflate(LayoutInflater.from(getContext()),
                binding.flAnswer, false);
        prescriptionBinding.setData(data);
        return prescriptionBinding.getRoot();
    }

    /**
     * 描述性回答
     *
     * @param binding
     * @param answer
     */
    @Override
    protected void setFill(ItemAnswerBinding binding, Answer answer) {

    }
}

