package com.doctor.sun.im.observer;

import com.doctor.sun.im.NIMConnectionState;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by rick on 26/5/2016.
 */
public class ReceiveMsgObserver implements Observer<List<IMMessage>> {
    @Override
    public void onEvent(List<IMMessage> imMessages) {
        for (IMMessage imMessage : imMessages) {
            boolean haveRead = NIMConnectionState.passThirtyMinutes(imMessage);
            NIMConnectionState.saveMsg(imMessage, haveRead);
        }
    }
}
