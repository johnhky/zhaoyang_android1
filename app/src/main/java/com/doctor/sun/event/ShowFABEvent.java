package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 2/12/2016.
 */
public class ShowFABEvent implements Event{

    public ShowFABEvent() {

    }

    public ShowFABEvent(String id) {
        this.id = id;
    }

    private String id;

    public String getId() {
        return id;
    }
}
