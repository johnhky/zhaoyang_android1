package com.doctor.sun.ui.activity.patient.handler;

import android.content.Intent;
import android.view.View;

import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.activity.patient.MedicalRecordDetailActivity;
import com.doctor.sun.ui.activity.patient.UrgentCallActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;

/**
 * Created by lucas on 1/7/16.
 */
public class RecordListHandler {
    private MedicalRecord data;

    public RecordListHandler(MedicalRecord medicalRecord) {
        data = medicalRecord;
    }

    public OnItemClickListener updateRecord() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Intent intent = MedicalRecordDetailActivity.makeIntent(view.getContext(), data);
                view.getContext().startActivity(intent);
            }
        };
    }

    public void applyAppointment(View view) {
        Intent intent = UrgentCallActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void select(View view) {
        view.setSelected(!view.isSelected());
    }
}
