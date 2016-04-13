package com.doctor.sun.entity.im;


import android.support.annotation.IntDef;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.im.custom.AttachmentData;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuntongxun.ecsdk.ECMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 12/10/15.
 */
public class TextMsg extends RealmObject implements LayoutId {
    // 多端统一
    public static final int Guess = 1;
    public static final int SnapChat = 2;
    public static final int Sticker = 3;
    public static final int RTS = 4;

    public static final int IMAGE = 11;
    public static final int AUDIO = 12;
    private static final int VIDEO = 13;
    public static final int FILE = 14;

    public static final String TAG = TextMsg.class.getSimpleName();
    public static final String DIRECTION_SEND = "SEND";
    public static final String DIRECTION_RECEIVE = "RECEIVE";
    public static final String ADMIN_DRUG = "[\"admin\",\"drug\"]";


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
    private int imageWidth;
    private int imageHeight;
    @Ignore
    private int itemLayoutId = -1;
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
        result.setUserData(msg.getUserData().replaceAll("\\s*|\t|\r|\n", ""));
        result.setMessageStatus(msg.getMsgStatus().toString());
        result.setIsAnonymity(msg.isAnonymity());
        return result;
    }

    public static TextMsg fromYXMessage(IMMessage msg) {
        TextMsg result = new TextMsg();
        result.setMsgId(msg.getUuid());
        result.setSessionId(msg.getSessionId());
        result.setType(msg.getMsgType().toString());
        if (msg.getDirect().equals(MsgDirectionEnum.In)) {
            result.setDirection(DIRECTION_RECEIVE);
        } else if (msg.getDirect().equals(MsgDirectionEnum.Out)) {
            result.setDirection(DIRECTION_SEND);
        }
        result.setBody(msg.getContent());
        result.setTime(msg.getTime());
        result.setFrom(msg.getFromAccount());
        String pushContent = msg.getPushContent();
        if (pushContent != null && pushContent.equals("用药信息提醒")) {
            result.setUserData(ADMIN_DRUG);
        }
        AttachmentData s = parseAttachment(msg);
        if (s != null) {
            result.setBody(s.getMsg());
            result.setMessageStatus(s.getData());
            result.setType(String.valueOf(s.getType()));
            result.setImageHeight(s.getImageHeight());
            result.setImageWidth(s.getImageWidth());
        }
        return result;
    }

    public static AttachmentData parseAttachment(IMMessage msg) {
        MsgAttachment attachment = msg.getAttachment();
        AttachmentData result = new AttachmentData();
        if (attachment instanceof ImageAttachment) {
            Log.e(TAG, "parseAttachment: image");
            ImageAttachment imageAttachment = (ImageAttachment) attachment;
            result.setType(IMAGE);
            result.setData(imageAttachment.getUrl());
            result.setMsg("照片");
            int imageWidth = imageAttachment.getWidth();
            int imageHeight = imageAttachment.getHeight();
            while (imageWidth > 300 || imageHeight > 800) {
                if (imageWidth < 200 || imageHeight < 200) break;
                imageWidth /= 2;
                imageHeight /= 2;
            }
            result.setImageWidth(imageWidth);
            result.setImageHeight(imageHeight);
            return result;
        } else if (attachment instanceof CustomAttachment) {
            result = parseCustom(msg);
        } else if (attachment instanceof AudioAttachment) {
            result.setType(AUDIO);
            Log.e(TAG, "parseAttachment: audio");
        } else if (attachment instanceof VideoAttachment) {
            result.setType(VIDEO);
            Log.e(TAG, "parseAttachment: video");
        } else if (attachment instanceof FileAttachment) {
            Log.e(TAG, "parseAttachment: file");
        }

        return result;
    }

    private static AttachmentData parseCustom(IMMessage msg) {
        MsgAttachment attachment = msg.getAttachment();
        AttachmentData result = new AttachmentData();
        if (attachment != null) {
            String json = attachment.toJson(true);
            try {
                JSONObject object = new JSONObject(json);

                int type = object.getInt("type");
//                JSONObject data = object.getJSONObject(KEY_DATA);
                switch (type) {
                    case TextMsg.Sticker: {
                        result.setMsg("贴图");
                        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(CustomAttachment.class, StickerAttachment.class);
                        CustomAttachment<StickerAttachment> customAttachment = JacksonUtils.fromJson(object.toString(), javaType);
                        StickerAttachment sticker = customAttachment.getData();
                        String text = (StickerManager.FILE_ANDROID_ASSET_STICKER + sticker.getCatalog() + "/" + sticker.getChartlet() + ".png");
//                        textAttachment.setData(text);
                        result.setData(text);
                        result.setType(TextMsg.Sticker);
                        return result;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        } else {
            Log.e(TAG, "parseCustom: " + null);
        }
        return null;
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

    @Override
    public int getItemLayoutId() {
        if (getType().equals(String.valueOf(Sticker))) {
            if (DIRECTION_SEND.equals(getDirection())) {
                return R.layout.msg_sticker_send;
            } else if (DIRECTION_RECEIVE.equals(getDirection())) {
                return R.layout.msg_sticker_receive;
            }
        }
        if (getType().equals(String.valueOf(IMAGE))) {
            if (DIRECTION_SEND.equals(getDirection())) {
                return R.layout.msg_image_send;
            } else if (DIRECTION_RECEIVE.equals(getDirection())) {
                return R.layout.msg_image_receive;
            }
        }
        if (DIRECTION_SEND.equals(getDirection())) {
            if (ADMIN_DRUG.equals(getUserData())) {
                return R.layout.msg_prescription_list;
            }
            return R.layout.msg_text_send;
        } else if (DIRECTION_RECEIVE.equals(getDirection())) {
            if (ADMIN_DRUG.equals(getUserData())) {
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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Guess, SnapChat, Sticker, RTS})
    public @interface AttachmentType {
    }
}
