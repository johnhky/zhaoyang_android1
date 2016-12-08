package com.doctor.sun.vm;

import android.view.View;

/**
 * Created by rick on 22/8/2016.
 */

public class ItemCaptchaInput extends ItemTextInput2 {

    private View.OnClickListener listener;

    public ItemCaptchaInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }


    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
