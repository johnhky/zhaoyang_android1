package com.doctor.sun.im;

import io.realm.RealmObject;

/**
 * Created by rick on 25/7/2016.
 */

public class AttachmentPair extends RealmObject {


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
}
