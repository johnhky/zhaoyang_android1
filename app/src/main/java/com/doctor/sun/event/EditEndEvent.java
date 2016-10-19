package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 28/9/2016.
 */
public class EditEndEvent implements Event {
    public final String id;

    public EditEndEvent(String id) {
        this.id = id;
    }
}
