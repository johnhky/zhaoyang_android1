package com.doctor.sun.event;

import android.content.Intent;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 20/10/2016.
 */

public class ActivityResultEvent implements Event {


    private final Intent data;
    private final int resultCode;
    private final int requestCode;

    public ActivityResultEvent(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }

    public Intent getData() {
        return data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
