package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 21/4/2016.
 */
public class ProgressEvent implements Event {
    private final int totalLength;
    private final int totalRead;

    public ProgressEvent(int totalRead, int totalLength) {
        this.totalLength = totalLength;
        this.totalRead = totalRead;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getTotalRead() {
        return totalRead;
    }
}
