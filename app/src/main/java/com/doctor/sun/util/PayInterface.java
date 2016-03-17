package com.doctor.sun.util;

import android.app.Activity;
import android.view.View;

import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 25/1/2016.
 */
public interface PayInterface {
    void payWithAlipay(Activity activity);

    void payWithWeChat(Activity activity);

    void simulatedPay(BaseAdapter component, View view, BaseViewHolder vh);
}
