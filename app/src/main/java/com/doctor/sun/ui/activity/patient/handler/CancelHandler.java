package com.doctor.sun.ui.activity.patient.handler;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.handler.BaseHandler;

/**
 * Created by lucas on 1/20/16.
 */
public class CancelHandler extends BaseHandler implements LayoutId {
    private Dialog dialog;

    public CancelHandler(Activity context, Dialog dialog) {
        super(context);
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
