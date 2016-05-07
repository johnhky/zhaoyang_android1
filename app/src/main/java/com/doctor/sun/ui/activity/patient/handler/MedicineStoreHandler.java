package com.doctor.sun.ui.activity.patient.handler;

import android.content.Intent;
import android.view.View;

import com.doctor.sun.entity.MedicineStore;
import com.doctor.sun.ui.activity.patient.MedicineHelperActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;

/**
 * Created by lucas on 1/29/16.
 */
public class MedicineStoreHandler {
    private MedicineStore data;

    public MedicineStoreHandler(MedicineStore medicineHelper) {
        data = medicineHelper;
    }

    public OnItemClickListener medicineHelper() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Intent intent = MedicineHelperActivity.makeIntent(view.getContext(), adapter.getItemCount() - 2);
                view.getContext().startActivity(intent);
            }
        };
    }
}
