package com.doctor.sun.ui.adapter;

import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.widget.SelectRecordDialog;

/**
 * Created by rick on 20/1/2016.
 */
public class SelectRecordAdapter extends SimpleAdapter {
    private final SelectRecordDialog dialog;

    public SelectRecordAdapter( SelectRecordDialog listener) {
        super();
        this.dialog = listener;
    }

    public void onRecordSelected(BaseViewHolder vh) {
        dialog.getListener().onSelectRecord(dialog, (MedicalRecord) get(vh.getAdapterPosition()));
    }
}
