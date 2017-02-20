package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 20/2/2017.
 */
public class OnTitleChangedEvent implements Event {
    public String title;

    public OnTitleChangedEvent(String title) {
        this.title = title;
    }
}
