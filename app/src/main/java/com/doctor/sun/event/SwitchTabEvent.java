package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 12/22/15.
 */
public class SwitchTabEvent implements Event {
    private final int position;

    public SwitchTabEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
