package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 1/2/2016.
 */
public class SaveAnswerSuccessEvent implements Event {
    private final String id;

    public SaveAnswerSuccessEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
