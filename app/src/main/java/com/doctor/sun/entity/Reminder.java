package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.vm.LayoutId;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by rick on 18/6/2016.
 */
public class Reminder implements LayoutId {

    /**
     * time : 日期字符串
     * content : 提醒内容
     */

    @JsonProperty("time")
    public String time;
    @JsonProperty("content")
    public String content;

    public Reminder() {
    }

    public Reminder(String time, String content) {
        this.time = time;
        this.content = content;
    }

    @JsonIgnore
    @Override
    public int getItemLayoutId() {
        return R.layout.item_r_reminder;
    }

    public static Reminder fromMap(Map<String, String> stringStringMap) {
        return JacksonUtils.fromMap(stringStringMap, Reminder.class);
    }
}
