package com.doctor.sun.im.observer;

import com.doctor.sun.im.NIMConnectionState;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by rick on 26/5/2016.
 */
public class MsgStatusObserver implements Observer<IMMessage> {
    @Override
    public void onEvent(IMMessage imMessage) {
        if (imMessage.getMsgType().equals(MsgTypeEnum.audio)) {
            if (imMessage.getStatus().equals(MsgStatusEnum.read)) {
                NIMConnectionState.saveMsg(imMessage, true);
            } else {
                if (imMessage.getDirect().equals(MsgDirectionEnum.In)) {
                    boolean haveRead = NIMConnectionState.passThirtyMinutes(imMessage);
                    NIMConnectionState.saveMsg(imMessage, haveRead);
                } else {
                    NIMConnectionState.saveMsg(imMessage, true);
                }
            }
        } else {
            NIMConnectionState.saveMsg(imMessage, true);
        }
    }
}
