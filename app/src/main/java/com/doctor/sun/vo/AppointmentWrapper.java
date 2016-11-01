package com.doctor.sun.vo;

import android.content.Context;

import com.doctor.sun.entity.Tags;
import com.doctor.sun.immutables.Appointment;

import java.util.List;

/**
 * Created by rick on 19/9/2016.
 */

public class AppointmentWrapper extends BaseItem {

    public final Appointment appointment;

    public AppointmentWrapper(int itemLayoutId, Appointment appointment) {
        super(itemLayoutId);
        this.appointment = appointment;
    }

    public Appointment getData() {
        return appointment;
    }

    public double getNeedPay(Context context) {
        return appointment.getNeed_pay();
    }

    public String getBookTime() {
        return appointment.getBook_time();
    }

    public String getDisplayType() {
        return appointment.getDisplay_type();
    }

    public String tagsSelectedStatus() {
        List<Tags> selectTags = appointment.getSelect_tags();
        if (selectTags == null || selectTags.isEmpty()) {
            return "没有选择任何咨询标签";
        }

        return "已选择" + selectTags.size() + "个咨询标签：";
    }

    public String tagsLabel() {
        List<Tags> selectTags = appointment.getSelect_tags();
        if (selectTags == null || selectTags.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Tags selectTag : selectTags) {
            sb.append(selectTag.tagName).append(" ");
        }
        return sb.toString();
    }
}
