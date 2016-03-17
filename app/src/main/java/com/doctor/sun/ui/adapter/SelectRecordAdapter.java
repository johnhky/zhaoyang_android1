package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.view.View;

import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.SelectRecordDialog;

/**
 * Created by rick on 20/1/2016.
 */
public class SelectRecordAdapter extends SimpleAdapter {
    private final SelectRecordDialog dialog;

    public SelectRecordAdapter(Context context, SelectRecordDialog listener) {
        super(context);
        this.dialog = listener;
    }

    public OnItemClickListener selectRecord() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                dialog.getListener().onSelectRecord(dialog, (MedicalRecord) get(vh.getAdapterPosition()));
            }
        };
    }
}
