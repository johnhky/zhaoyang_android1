package com.doctor.sun.event;

import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.IntBoolean;

import java.util.HashMap;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 12/10/2016.
 */
public class PayFailEvent implements Event {

    private String money;
    private boolean payWithWechat;
    private HashMap<String, String> extraField;
    private Appointment data;
    private int payAppointment;

    public PayFailEvent(String money, boolean b, HashMap<String, String> extraField) {
        payAppointment = IntBoolean.FALSE;
        this.money = money;
        this.payWithWechat = b;
        this.extraField = extraField;
    }

    public PayFailEvent(Appointment data, boolean b) {
        payAppointment = IntBoolean.TRUE;
        this.data = data;
        this.payWithWechat = b;
    }

    public String getMoney() {
        return money;
    }

    public boolean isPayWithWechat() {
        return payWithWechat;
    }

    public HashMap<String, String> getExtraField() {
        return extraField;
    }

    public Appointment getData() {
        return data;
    }

    public int getPayAppointment() {
        return payAppointment;
    }
}
