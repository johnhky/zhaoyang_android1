package com.doctor.sun.vm;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.doctor.sun.R;

/**
 * Created by rick on 7/4/2016.
 */
public class ClickMenu extends BaseMenu {
    private View.OnClickListener listener;

    public ClickMenu(@LayoutRes int itemLayoutId, @DrawableRes int icon, String title, View.OnClickListener listener) {
        super(itemLayoutId, icon, title);
        this.listener = listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View.OnClickListener itemClick() {
        return listener;
    }

    public int showDrawable() {
        if (isEnabled()) {
            return R.drawable.ic_enter;
        } else {
            return R.drawable.bg_transparent;
        }
    }

    public String showHint() {
        if (isEnabled()) {
            return "更换手机号码";
        } else {
            return "";
        }
    }

}
