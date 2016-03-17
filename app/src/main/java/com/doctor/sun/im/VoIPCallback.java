package com.doctor.sun.im;

import android.util.Log;

import com.doctor.sun.event.CallAlertEvent;
import com.doctor.sun.event.CallAnsweredEvent;
import com.doctor.sun.event.CallFailedEvent;
import com.doctor.sun.event.CallReleasedEvent;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.VideoRatio;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 1/2/2016.
 */
public class VoIPCallback implements ECVoIPCallManager.OnVoIPListener {
    public static final String TAG = VoIPCallback.class.getSimpleName();

    @Override
    public void onDtmfReceived(String s, char c) {

    }

    @Override
    public void onCallEvents(ECVoIPCallManager.VoIPCall voIPCall) {
        if (voIPCall == null) return;

        switch (voIPCall.callState) {
            case ECCALL_ALERTING:
                EventHub.post(new CallAlertEvent(voIPCall));
                break;
            case ECCALL_PROCEEDING:
                Log.e(TAG, "onCallEvents: ECCALL_PROCEEDING" );
                break;
            case ECCALL_ANSWERED:
                EventHub.post(new CallAnsweredEvent(voIPCall));
                break;
            case ECCALL_FAILED://
                EventHub.post(new CallFailedEvent(voIPCall));
                break;
            case ECCALL_RELEASED:
                EventHub.post(new CallReleasedEvent(voIPCall));
                break;
            default:
                EventHub.post(new CallReleasedEvent(voIPCall));
                break;
        }
    }

    @Override
    public void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {

    }

    @Override
    public void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {

    }

    @Override
    public void onVideoRatioChanged(VideoRatio videoRatio) {

    }
}
