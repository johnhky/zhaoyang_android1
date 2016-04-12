package com.doctor.sun.im.custom;

/**
 * Created by rick on 11/4/2016.
 */
public class AttachmentData {
    private int type;
    private String data;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
}
