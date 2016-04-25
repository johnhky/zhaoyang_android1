package com.doctor.sun.entity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by rick on 11/4/2016.
 */
public interface NimTeamId {
    SessionTypeEnum getType();

    String getTeamId();

    String getP2PId();
}
