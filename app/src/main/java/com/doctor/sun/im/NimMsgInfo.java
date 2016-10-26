package com.doctor.sun.im;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by rick on 11/4/2016.
 */
public interface NimMsgInfo {
    SessionTypeEnum getType();

    String getTeamId();

    String getTargetP2PId();

    boolean enablePush();

    int appointmentId();

    boolean shouldAskServer();

    int getDuration();
}
