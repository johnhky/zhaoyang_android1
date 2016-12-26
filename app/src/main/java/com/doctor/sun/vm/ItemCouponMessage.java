package com.doctor.sun.vm;

import com.doctor.sun.R;

/**
 * Created by kb on 26/12/2016.
 */

public class ItemCouponMessage extends BaseItem {

    private String message;

    public ItemCouponMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_coupon_message;
    }
}
