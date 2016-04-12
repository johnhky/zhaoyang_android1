package com.doctor.sun.im.custom;

import com.doctor.sun.util.JacksonUtils;

/**
 * Created by rick on 11/4/2016.
 */
public class TextAttachment implements com.netease.nimlib.sdk.msg.attachment.MsgAttachment {
    private String data;
    private int type;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toJson(boolean b) {
        return JacksonUtils.toJson(this);
    }
}
