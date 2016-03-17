package com.doctor.sun.event;

import io.ganguo.library.core.event.Event;

/**
 * Created by Lynn on 1/8/16.
 */
public class CloseDialogEvent implements Event {
    private boolean mIsClose;

    public CloseDialogEvent(boolean isClose) {
        mIsClose = isClose;
    }

    public boolean isClose() {
        return mIsClose;
    }

    public void setIsClose(boolean mIsClose) {
        this.mIsClose = mIsClose;
    }
}
