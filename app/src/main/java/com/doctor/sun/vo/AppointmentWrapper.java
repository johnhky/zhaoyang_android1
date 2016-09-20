package com.doctor.sun.vo;

import android.content.Context;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Tags;

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

    public int getNeedPay(Context context) {
        return Integer.parseInt(appointment.getNeedPay());
    }

    public String getBookTime() {
        return appointment.getBookTime();
    }

    public String getDisplayType() {
        return appointment.getDisplayType();
    }

    public String tagsSelectedStatus() {
        List<Tags> selectTags = appointment.selectTags;
        if (selectTags == null || selectTags.isEmpty()) {
            return "没有选择任何咨询标签";
        }

        return "已选择" + selectTags.size() + "个咨询标签：";
    }

    public String tagsLabel() {
        List<Tags> selectTags = appointment.selectTags;
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
