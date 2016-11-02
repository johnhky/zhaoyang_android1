package com.doctor.sun.entity.im;

import android.support.annotation.NonNull;

import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.im.AttachmentPair;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by rick on 13/4/2016.
 */
public class TextMsgFactory {
    public static final String DIRECTION_SEND = "SEND";
    public static final String DIRECTION_RECEIVE = "RECEIVE";
    public static final String ADMIN_DRUG = "[\"admin\",\"drug\"]";
    public static final int ONE_SECOND = 1000;

    //attachment 字段
    public static final String ATTACHMENT_TYPE = "type";
    public static final String ATTACHMENT_URL = "url";
    public static final String DURATION = "duration";
    public static final String BODY = "body";
    public static final String DATA = "data";
    public static final String EXTENSION = "extension";
    public static final String FILE_SIZE = "fileSize";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String ATTACHMENT_PATH = "path";
    private static boolean refreshMsg;


    public static TextMsg fromYXMessage(IMMessage msg) {
        if (msg.getMsgType().equals(MsgTypeEnum.notification)) {
            return null;
        }

        TextMsg result = new TextMsg();
        result.setMsgId(msg.getUuid());
        result.setSessionId(msg.getSessionId());
        result.setType(msg.getMsgType().toString());
        if (msg.getDirect().equals(MsgDirectionEnum.In)) {
            result.setDirection(DIRECTION_RECEIVE);
        } else if (msg.getDirect().equals(MsgDirectionEnum.Out)) {
            result.setDirection(DIRECTION_SEND);
        }
        result.setMessageStatus(msg.getStatus().toString());
        result.setSessionType(msg.getSessionType().toString());
        result.setBody(msg.getContent());
        result.setTime(msg.getTime());
        result.setFrom(msg.getFromAccount());
        String pushContent = msg.getPushContent();
        if (pushContent != null && pushContent.equals("处方信息提醒")) {
            result.setUserData(ADMIN_DRUG);
        }
        RealmList<AttachmentPair> attachment = parseAttachment2(msg);
        if (attachment != null) {
            result.setAttachment(attachment);
            result.setType(attachment.get(0).getValue());
        }

//        if (msg.getMsgType().equals(MsgTypeEnum.notification)) {
//            String msgShowText = TeamNotificationHelper.getTeamNotificationText(msg, "");
//            result.setBody(msgShowText);
//        }
        return result;
    }

    private static RealmList<AttachmentPair> parseAttachment2(IMMessage msg) {
        MsgAttachment attachment = msg.getAttachment();
        if (attachment instanceof ImageAttachment) {
            return parseImageAttachment(msg, (ImageAttachment) attachment);
        } else if (attachment instanceof CustomAttachment) {
            return parseCustomAttachment(msg);
        } else if (attachment instanceof AudioAttachment) {
            return parseAudio2(msg, (AudioAttachment) attachment);
        } else if (attachment instanceof VideoAttachment) {
            return parseVideoAttachment(msg, (VideoAttachment) attachment);
        } else if (attachment instanceof FileAttachment) {
            return parseFileAttachment(msg, (FileAttachment) attachment);
        } else {
            return null;
        }
    }

    private static RealmList<AttachmentPair> parseAudio2(IMMessage msg, AudioAttachment attachment) {
        RealmList<AttachmentPair> result = new RealmList<>();

        long duration = attachment.getDuration() / ONE_SECOND;
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.AUDIO)));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_URL, attachment.getUrl()));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_PATH, "file://" + attachment.getPath()));
        result.add(createAttachmentPair(msg.getUuid() + DURATION, String.valueOf(duration)));

        return result;
    }

    private static RealmList<AttachmentPair> parseCustomAttachment(IMMessage msg) {
        CustomAttachment attachment = (CustomAttachment) msg.getAttachment();
        RealmList<AttachmentPair> result = new RealmList<>();

        int type = attachment.getType();
//                JSONObject data = object.getJSONObject(KEY_DATA);
        switch (type) {
            case TextMsg.Sticker: {
                result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.Sticker)));
                result.add(createAttachmentPair(msg.getUuid() + BODY, "照片"));
                StickerAttachment sticker = (StickerAttachment) attachment.getData();
                String text = (StickerManager.FILE_ANDROID_ASSET_STICKER + sticker.getCatalog() + "/" + sticker.getChartlet() + ".png");
                result.add(createAttachmentPair(msg.getUuid() + DATA, text));

                return result;
            }
            case TextMsg.Drug: {
                JSONObject data = (JSONObject) attachment.getData();
                String msg1 = data.toString();
                result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.Drug)));
                result.add(createAttachmentPair(msg.getUuid() + BODY, msg1));
                return result;
            }
            case TextMsg.DrugV2: {
                JSONObject data = (JSONObject) attachment.getData();
                String msg1 = data.toString();
                result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.DrugV2)));
                result.add(createAttachmentPair(msg.getUuid() + BODY, msg1));
                return result;
            }
            case TextMsg.EXTEND_TIME: {
                return null;
            }
            case TextMsg.REFRESH_APPOINTMENT: {
                ArrayList<AttachmentPair> data = (ArrayList<AttachmentPair>) attachment.getData();
                result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.REFRESH_APPOINTMENT)));
                for (int i = 0; i < data.size(); i++) {
                    AttachmentPair attachmentPair = data.get(i);
                    attachmentPair.setKey(msg.getUuid() + attachmentPair.getKey());
                }
                result.addAll(data);
                return result;
            }
        }
        return null;
    }

    private static RealmList<AttachmentPair> parseImageAttachment(IMMessage msg, ImageAttachment attachment) {
        RealmList<AttachmentPair> result = new RealmList<>();

        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.IMAGE)));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_URL, attachment.getUrl()));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_PATH, "file://" + attachment.getPath()));
        result.add(createAttachmentPair(msg.getUuid() + BODY, "照片"));
        int imageWidth = attachment.getWidth();
        int imageHeight = attachment.getHeight();
        while (imageWidth > 300 || imageHeight > 800) {
            imageWidth /= 2;
            imageHeight /= 2;
        }
        while (imageWidth < 150 || imageHeight < 400) {
            imageWidth *= 2;
            imageHeight *= 2;
        }
        result.add(createAttachmentPair(msg.getUuid() + WIDTH, String.valueOf(imageWidth)));
        result.add(createAttachmentPair(msg.getUuid() + HEIGHT, String.valueOf(imageHeight)));
        return result;
    }

    private static RealmList<AttachmentPair> parseFileAttachment(IMMessage msg, FileAttachment attachment) {
        RealmList<AttachmentPair> result = new RealmList<>();

        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.FILE)));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_URL, attachment.getUrl()));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_PATH, "file://" + attachment.getPath()));
        result.add(createAttachmentPair(msg.getUuid() + BODY, attachment.getDisplayName()));
        result.add(createAttachmentPair(msg.getUuid() + EXTENSION, attachment.getExtension()));
        result.add(createAttachmentPair(msg.getUuid() + FILE_SIZE, String.valueOf(attachment.getSize())));
        return result;
    }

    private static RealmList<AttachmentPair> parseVideoAttachment(IMMessage msg, VideoAttachment attachment) {
        RealmList<AttachmentPair> result = new RealmList<>();

        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_TYPE, String.valueOf(TextMsg.VIDEO)));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_URL, attachment.getUrl()));
        result.add(createAttachmentPair(msg.getUuid() + ATTACHMENT_PATH, "file://" + attachment.getPath()));
        result.add(createAttachmentPair(msg.getUuid() + BODY, "视频"));
        result.add(createAttachmentPair(msg.getUuid() + EXTENSION, attachment.getExtension()));
        result.add(createAttachmentPair(msg.getUuid() + FILE_SIZE, String.valueOf(attachment.getSize())));
        return result;
    }

    @NonNull
    private static AttachmentPair createAttachmentPair(String key, String value) {
        AttachmentPair object = new AttachmentPair();
        object.setKey(key);
        object.setValue(value);
        return object;
    }

    public static boolean isRefreshMsg(String type) {
        return String.valueOf(TextMsg.REFRESH_APPOINTMENT).equals(type)
                || "follow_up_start".equals(type)
                || "follow_up_end".equals(type)
                || "appointment_start".equals(type)
                || "appointment_end".equals(type);
    }
}
