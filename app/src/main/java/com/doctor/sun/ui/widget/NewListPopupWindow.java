package com.doctor.sun.ui.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by heky on 17/7/12.
 */

public class NewListPopupWindow extends PopupWindow {

    public NewListPopupWindow(Context context) {
        super(context, null);
    }

    @Override
    public void showAsDropDown(View anchor) {

        super.showAsDropDown(anchor);

    }
}
