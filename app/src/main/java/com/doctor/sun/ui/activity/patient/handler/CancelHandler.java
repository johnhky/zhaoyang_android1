package com.doctor.sun.ui.activity.patient.handler;

import android.app.Dialog;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.vo.LayoutId;

/**
 * Created by lucas on 1/20/16.
 */
public class CancelHandler implements LayoutId {
    private Dialog dialog;

    public CancelHandler(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_cancel;
    }

    public void cancel(View view) {
        dialog.dismiss();
    }
}
