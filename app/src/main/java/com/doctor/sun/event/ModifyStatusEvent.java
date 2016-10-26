package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 28/9/2016.
 */
public class ModifyStatusEvent implements Event {
    public final String id;
    public final int status;

    public ModifyStatusEvent(String id, int status) {
        this.id = id;
        this.status = status;
    }
}
