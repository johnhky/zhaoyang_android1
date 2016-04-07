package com.doctor.sun.vo;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by rick on 7/4/2016.
 */
public class ClickMenu extends BaseMenu {
    private View.OnClickListener listener;

    public ClickMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title, View.OnClickListener listener) {
        super(itemLayoutId, icon, title);
        this.listener = listener;
    }


    @Override
    public View.OnClickListener itemClick() {
        return listener;
    }
}
