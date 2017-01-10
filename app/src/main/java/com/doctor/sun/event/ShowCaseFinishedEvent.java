package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 29/6/2016.
 */
public class ShowCaseFinishedEvent implements Event {
    public String id;

    public ShowCaseFinishedEvent(String id) {
        this.id = id;
    }

    public ShowCaseFinishedEvent(int id) {
        this.id = String.valueOf(id);
    }

}
