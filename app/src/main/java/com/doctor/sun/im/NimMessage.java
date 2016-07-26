package com.doctor.sun.im;

import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import io.realm.RealmList;

/**
 * Created by rick on 25/7/2016.
 */

public class NimMessage implements LayoutId {
    private String uuid;

    private int msgType;
    private int direction;
    private int layoutId;
    private long time;
    private String sessionId;
    private String content;
    private String fromAccount;
    private String pushContent;
    private RealmList<AttachmentPair> attachment;


    public String getUuid() {
        return uuid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getMsgType() {
        return msgType;
    }

    public int getDirection() {
        return direction;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getPushContent() {
        return pushContent;
    }

    public RealmList<AttachmentPair> getAttachment() {
        return attachment;
    }

    public void setAttachment(RealmList<AttachmentPair> attachment) {
        this.attachment = attachment;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }
}
