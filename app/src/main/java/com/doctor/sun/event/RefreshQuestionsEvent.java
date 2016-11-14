package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 14/11/2016.
 */

public class RefreshQuestionsEvent implements Event {
    private final String id;

    public RefreshQuestionsEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
