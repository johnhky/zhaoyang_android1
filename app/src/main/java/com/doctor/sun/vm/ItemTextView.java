package com.doctor.sun.vm;

import android.view.View;

import com.doctor.sun.R;

/**
 * Created by heky on 17/4/24.
 */

public class ItemTextView extends ItemTextInput {

    View.OnClickListener listener;

    public ItemTextView(int itemLayoutId, String title) {
        super(itemLayoutId, title);
    }


    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
