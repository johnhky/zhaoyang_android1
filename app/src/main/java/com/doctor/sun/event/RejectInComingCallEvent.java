package com.doctor.sun.event;

import com.doctor.sun.entity.constans.CommunicationType;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 13/7/2016.
 */
public class RejectInComingCallEvent implements Event {
    private int type;
    private final String account;

    public RejectInComingCallEvent(String account,int type) {
        this.account = account;
        this.type = type;
    }

    @CommunicationType
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
