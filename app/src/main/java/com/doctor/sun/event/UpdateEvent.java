package com.doctor.sun.event;

import com.doctor.sun.entity.Version;

import io.ganguo.library.core.event.Event;

/**
 * Created by kb on 16-10-13.
 */

public class UpdateEvent implements Event{

    private Version data;
    private String versionName;

    public UpdateEvent(Version data, String versionName) {
        this.data = data;
        this.versionName = versionName;
    }

    public Version getData() {
        return data;
    }

    public String getVersionName() {
        return versionName;
    }
}
