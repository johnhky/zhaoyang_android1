package com.doctor.sun.entity.im;


import android.support.annotation.IntDef;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by rick on 12/10/15.
 */
public class TextMsg extends RealmObject implements LayoutId {
    // 多端统一
    public static final int Guess = 1;
    public static final int SnapChat = 2;
    public static final int Sticker = 3;
    public static final int RTS = 4;
    public static final int EXTEND_TIME = 98;
    public static final int Drug = 99;

    public static final int IMAGE = 11;
    public static final int AUDIO = 12;
    public static final int VIDEO = 13;
    public static final int FILE = 14;

    public static final String TAG = TextMsg.class.getSimpleName();
    public static final MsgHandler handler = new MsgHandler();


    private long id;
    private String sessionId;
    private String type;
    private String direction;
    private String body;
    @Required
    @PrimaryKey
    private String msgId;
    private long time;
    private String nickName;
    private String from;
    private String to;
    private String userData;
    private String messageStatus;
    private int version;
    private boolean finished;
    private boolean isAnonymity;
    private boolean haveRead;
    private boolean haveListen;
    private int imageWidth;
    private int imageHeight;
    private long duration;
    @Ignore
    private int itemLayoutId = -1;
    @Ignore
    private String avatar;


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

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int getItemLayoutId() {
        if (getType().equals(String.valueOf(MsgTypeEnum.notification))) {
            return R.layout.msg_notification;
        }
        if (TextMsgFactory.DIRECTION_SEND.equals(getDirection())) {
            if (getType().equals(String.valueOf(Sticker))) {
                return R.layout.msg_sticker_send;
            }
            if (getType().equals(String.valueOf(Drug))) {
                return R.layout.msg_prescription_list;
            }
            if (getType().equals(String.valueOf(IMAGE))) {
                return R.layout.msg_image_send;
            }
            if (getType().equals(String.valueOf(AUDIO))) {
                return R.layout.msg_audio_send;
            }
            if (getType().equals(String.valueOf(FILE))) {
                return R.layout.msg_file_send;
            }
            if (TextMsgFactory.ADMIN_DRUG.equals(getUserData())) {
                return R.layout.msg_prescription_list;
            }
            if (getType().equals(String.valueOf(TextMsg.EXTEND_TIME))) {
                return R.layout.msg_extend_time;
            }
            return R.layout.msg_text_send;
        }

        if (TextMsgFactory.DIRECTION_RECEIVE.equals(getDirection())) {
            if (getType().equals(String.valueOf(Sticker))) {
                return R.layout.msg_sticker_receive;
            }
            if (getType().equals(String.valueOf(Drug))) {
                return R.layout.msg_prescription_list;
            }
            if (getType().equals(String.valueOf(IMAGE))) {
                return R.layout.msg_image_receive;
            }
            if (getType().equals(String.valueOf(AUDIO))) {
                return R.layout.msg_audio_receive;
            }
            if (getType().equals(String.valueOf(FILE))) {
                return R.layout.msg_file_receive;
            }
            if (TextMsgFactory.ADMIN_DRUG.equals(getUserData())) {
                return R.layout.msg_prescription_list;
            }
            return R.layout.msg_text_receive;
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isHaveListen() {
        return haveListen;
    }

    public void setHaveListen(boolean haveListen) {
        this.haveListen = haveListen;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Guess, SnapChat, Sticker, RTS})
    public @interface AttachmentType {
    }
}
