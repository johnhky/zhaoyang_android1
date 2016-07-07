package com.doctor.sun.im.custom;

import com.doctor.sun.util.JacksonUtils;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class ExtendTimeAttachment implements MsgAttachment {


    private int appointmentId = -1;

    private long timeToCountDown = -1;
    private String countDownText;

    private String content;
    private String cancelText;
    private String confirmText;

    private int promiss;

    public ExtendTimeAttachment() {
    }

    public long getTimeToCountDown() {
        return timeToCountDown;
    }

    public void setTimeToCountDown(long timeToCountDown) {
        this.timeToCountDown = timeToCountDown;
    }

    public String getCountDownText() {
        return countDownText;
    }

    public void setCountDownText(String countDownText) {
        this.countDownText = countDownText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    @Override
    public String toJson(boolean b) {
        return JacksonUtils.toJson(this);
    }
}
