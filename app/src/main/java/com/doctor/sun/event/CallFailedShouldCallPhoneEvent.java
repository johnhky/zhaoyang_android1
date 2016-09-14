package com.doctor.sun.event;

import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 14/7/2016.
 */
public class CallFailedShouldCallPhoneEvent implements Event {

    private final int chatType;

    public CallFailedShouldCallPhoneEvent(AVChatType chatType) {
        this.chatType = chatType.getValue();
    }

    public int getChatType() {
        return chatType;
    }
}
