package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 12/10/2016.
 */
public class MainTabChangedEvent implements Event {
    private final int position;

    public MainTabChangedEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
