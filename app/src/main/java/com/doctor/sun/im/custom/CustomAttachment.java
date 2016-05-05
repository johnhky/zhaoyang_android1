package com.doctor.sun.im.custom;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachment<T> implements MsgAttachment {


    @TextMsg.AttachmentType
    @JsonProperty("type")
    private int type;
    @JsonProperty("data")
    private T data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toJson(boolean b) {
        return JacksonUtils.toJson(this);
    }

}
