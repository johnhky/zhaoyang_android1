package com.doctor.sun.im;

import com.doctor.sun.util.JacksonUtils;

import io.realm.RealmObject;

/**
 * Created by rick on 25/7/2016.
 */

public class AttachmentPair extends RealmObject {

    public AttachmentPair() {
    }

    public AttachmentPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

//    @Override
//    public String toJson(boolean b) {
//        return JacksonUtils.toJson(this);
//    }
}
