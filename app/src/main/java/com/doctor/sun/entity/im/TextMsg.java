package com.doctor.sun.entity.im;


import android.support.annotation.IntDef;

import com.doctor.sun.R;
import com.doctor.sun.im.AttachmentPair;
import com.doctor.sun.vm.LayoutId;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by rick on 12/10/15.
 */
public class TextMsg extends RealmObject implements LayoutId {
    // 多端统一
    public static final int STRING_MSG = -2;
    public static final int UNKNOWN = -1;
    public static final int Guess = 1;
    public static final int SnapChat = 2;
    public static final int Sticker = 3;
    public static final int RTS = 4;
    public static final int REFRESH_APPOINTMENT = 66;
    public static final int EXTEND_TIME = 98;
    public static final int Drug = 99;
    public static final int DrugV2 = 101;

    public static final int IMAGE = 11;
    public static final int AUDIO = 12;
    public static final int VIDEO = 13;
    public static final int FILE = 14;

    public static final String TAG = TextMsg.class.getSimpleName();
    public static final MsgHandler handler = new MsgHandler();
    //uuid
    @Required
    @PrimaryKey
    private String msgId;
    private long id;
    @Index
    private String sessionId;
    private String type;
    private String direction;
    //content
    private String body;
    private long time;
    private String nickName;
    //fromAccount
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
    private String sessionType;
    private RealmList<AttachmentPair> attachment;

    public RealmList<AttachmentPair> getAttachment() {
        return attachment;
    }

    public void setAttachment(RealmList<AttachmentPair> attachment) {
        this.attachment = attachment;
    }

    public String attachmentData(String fieldKey) {
        if (attachment == null) {
            return "";
        }
        if (attachment.isManaged()) {
            AttachmentPair pair = attachment.where().equalTo("key", msgId + fieldKey).findFirst();
            if (pair == null) return "";
            return pair.getValue();
        } else {
            return "";
        }

    }

    public int attachmentInt(String fieldKey) {
        return Integer.parseInt(attachmentData(fieldKey));
    }

    public long attachmentLong(String fieldKey) {
        return Long.parseLong(attachmentData(fieldKey));
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

    public boolean isFailed() {
        if (messageStatus == null) {
            return false;
        }
        return messageStatus.equals(MsgStatusEnum.fail.toString());
    }

    public boolean isSending() {
        if (messageStatus == null) {
            return false;
        }
        return messageStatus.equals(MsgStatusEnum.sending.toString());
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
        if (getType().equals(String.valueOf(UNKNOWN))) {
            return R.layout.msg_notification;
        }
        if (getType().equals(String.valueOf(MsgTypeEnum.notification))) {
            return R.layout.msg_notification;
        }
        if (TextMsgFactory.isRefreshMsg(getType())) {
            if (attachmentData("data").equals("医生建议/处方") && null != attachmentData("appointment_id")) {
                return R.layout.msg_record;
            } else {
                return R.layout.msg_notification;
            }

        }
        if (getType().equals(String.valueOf(DrugV2))) {
            return R.layout.msg_prescription_list;
        }
        if (getType().equals(String.valueOf(Drug))) {
            return R.layout.msg_prescription_list;
        }
        if (TextMsgFactory.ADMIN_DRUG.equals(getUserData())) {
            return R.layout.msg_prescription_list;
        }

        if (TextMsgFactory.DIRECTION_SEND.equals(getDirection())) {
            if (getType().equals(String.valueOf(Sticker))) {
                return R.layout.msg_sent_sticker;
            }
            if (getType().equals(String.valueOf(IMAGE))) {
                return R.layout.msg_sent_image;
            }
            if (getType().equals(String.valueOf(AUDIO))) {
                return R.layout.msg_sent_audio;
            }
            if (getType().equals(String.valueOf(VIDEO))) {
                return R.layout.msg_sent_file;
            }
            if (getType().equals(String.valueOf(FILE))) {
                return R.layout.msg_sent_file;
            }
            return R.layout.msg_sent_text;
        }

        if (TextMsgFactory.DIRECTION_RECEIVE.equals(getDirection())) {
            if (getType().equals(String.valueOf(Sticker))) {
                return R.layout.msg_receive_sticker;
            }
            if (getType().equals(String.valueOf(IMAGE))) {
                return R.layout.msg_receive_image;
            }
            if (getType().equals(String.valueOf(VIDEO))) {
                return R.layout.msg_receive_file;
            }
            if (getType().equals(String.valueOf(AUDIO))) {
                return R.layout.msg_receive_audio;
            }
            if (getType().equals(String.valueOf(FILE))) {
                return R.layout.msg_receive_file;
            }

            return R.layout.msg_receive_text;
        }

        return itemLayoutId;
    }


    public SessionTypeEnum getSessionTypeEnum() {
        if (getSessionType() == null) {
            return SessionTypeEnum.None;
        }
        return SessionTypeEnum.valueOf(getSessionType());
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

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getSessionType() {
        return sessionType;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Guess, SnapChat, Sticker, RTS})
    public @interface AttachmentType {
    }
}
