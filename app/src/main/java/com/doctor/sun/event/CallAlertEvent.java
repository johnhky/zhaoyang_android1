package com.doctor.sun.event;

import com.yuntongxun.ecsdk.ECVoIPCallManager;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 1/2/2016.
 */
public class CallAlertEvent implements Event {
    private final ECVoIPCallManager.VoIPCall voIPCall;

    public CallAlertEvent(ECVoIPCallManager.VoIPCall voIPCall) {
        this.voIPCall = voIPCall;
    }

    public ECVoIPCallManager.VoIPCall getVoIPCall() {
        return voIPCall;
    }
}
