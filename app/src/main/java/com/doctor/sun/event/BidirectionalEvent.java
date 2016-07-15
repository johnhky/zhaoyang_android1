package com.doctor.sun.event;

import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import io.ganguo.library.core.event.Event;

/**
 * Created by rick on 14/7/2016.
 */
public class BidirectionalEvent implements Event {

    private final int chatType;

    public BidirectionalEvent(AVChatType chatType) {
        this.chatType = chatType.getValue();
    }

    public int getChatType() {
        return chatType;
    }
}
