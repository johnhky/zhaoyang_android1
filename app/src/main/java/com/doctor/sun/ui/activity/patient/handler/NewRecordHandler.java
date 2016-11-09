package com.doctor.sun.ui.activity.patient.handler;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.patient.RecordListActivity;
import com.doctor.sun.vo.LayoutId;

/**
 * Created by lucas on 1/20/16.
 */
public class NewRecordHandler implements LayoutId {
    private Dialog dialog;

    public NewRecordHandler(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_new;
    }

    public void newRecord(View view) {
        Intent intent = RecordListActivity.makeIntent(view.getContext(), true);
        view.getContext().startActivity(intent);
        dialog.dismiss();
    }
}
