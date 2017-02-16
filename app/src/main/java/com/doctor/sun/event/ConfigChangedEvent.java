package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 15/2/2017.
 */

public class ConfigChangedEvent implements Event {

    private String key;

    public ConfigChangedEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
