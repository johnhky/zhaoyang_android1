package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.doctor.sun.R;

import io.ganguo.library.util.Systems;

/**
 * Created by rick on 10/26/15.
 */
public class BottomDialog {
    public static final String TAG = BottomDialog.class.getSimpleName();

    public static Dialog showDialog(final Activity context, View view) {

        Dialog dlg = new Dialog(context, R.style.BottomDialog);
        // set a large value put it in bottom
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(w.getAttributes());
        lp.x = 0;
//        final int cMakeBottom = -1000;
//        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        lp.width = Systems.getScreenWidth(context);
        lp.height = (int) (Systems.getScreenHeight(context) / 2.2);
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.5f;
        dlg.setContentView(view);
        dlg.show();
        dlg.getWindow().setAttributes(lp);

        return dlg;
    }


}
