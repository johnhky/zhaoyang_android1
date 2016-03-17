package com.doctor.sun.entity;

import java.util.List;

/**
 * Created by rick on 12/9/15.
 */
public class RecentAppointment {
    private int id;
    private String doctor_name;
    private String book_time;
    private String progress;
    private List<Object> return_info;

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setReturn_info(List<Object> return_info) {
        this.return_info = return_info;
    }

    public int getId() {
        return id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getBook_time() {
        return book_time;
    }

    public String getProgress() {
        return progress;
    }

    public List<Object> getReturn_info() {
        return return_info;
    }
}
