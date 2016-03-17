package com.doctor.sun.entity.im;


import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.yuntongxun.ecsdk.ECMessage;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 12/10/15.
 */
public class TextMsg extends RealmObject implements LayoutId {
    public static final String DIRECTION_SEND = "SEND";
    public static final String DIRECTION_RECEIVE = "RECEIVE";


    private long id;
    private String sessionId;
    private String type;
    private String direction;
    private String body;
    @PrimaryKey
    private String msgId;
    private long time;
    private String nickName;
    private String from;
    private String to;
    private String userData;
    private String messageStatus;
    private int version;
    private boolean isAnonymity;
    private boolean haveRead;
    @Ignore
    private int itemLayoutId = R.layout.item_message_send;
    @Ignore
    private String avatar;

    public static TextMsg fromECMessage(ECMessage msg) {
        TextMsg result = new TextMsg();
        result.setId(msg.getId());
        result.setSessionId(msg.getSessionId());
        result.setType(msg.getType().toString());
        result.setDirection(msg.getDirection().toString());
        String body = msg.getBody().toString();
        result.setBody(body.substring(19, body.length() - 1));
        result.setMsgId(msg.getMsgId());
        result.setTime(msg.getMsgTime());
        result.setNickName(msg.getNickName());
        result.setFrom(msg.getForm());
        result.setTo(msg.getTo());
        result.setUserData(msg.getUserData().replaceAll("\\s*|\t|\r|\n",""));
        result.setMessageStatus(msg.getMsgStatus().toString());
        result.setIsAnonymity(msg.isAnonymity());
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(boolean isAnonymity) {
        this.isAnonymity = isAnonymity;
    }

    @Override
    public int getItemLayoutId() {
        if (DIRECTION_SEND.equals(getDirection())) {
            return R.layout.item_message_send;
        } else if (DIRECTION_RECEIVE.equals(getDirection())) {
            if (getUserData().equals("[\"admin\",\"drug\"]")) {
                return R.layout.item_prescription_list;
            }
            return R.layout.item_message_receive;
        }
        return itemLayoutId;
    }

    public void setItemLayoutId(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isHaveRead() {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead) {
        this.haveRead = haveRead;
    }
}
