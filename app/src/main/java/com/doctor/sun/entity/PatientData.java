package com.doctor.sun.entity;

import java.util.ArrayList;

/**
 * Created by heky on 17/5/27.
 */

public class PatientData {
    public String id;
    public String name;
    public String phone;
    public ArrayList<String> record_names;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getRecord_names() {
        return record_names;
    }

    public void setRecord_names(ArrayList<String> record_names) {
        this.record_names = record_names;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
