package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by heky on 17/7/17.
 */

public class SaveAnswerFailedEvent implements Event {

    private final String id;
    private final String msg;

    public SaveAnswerFailedEvent(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }
}
