package com.doctor.sun.entity.im;

import android.support.annotation.NonNull;

import com.doctor.sun.dto.PrescriptionDTO;
import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.im.custom.AttachmentData;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.JsonAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuntongxun.ecsdk.ECMessage;

import org.json.JSONObject;

/**
 * Created by rick on 13/4/2016.
 */
public class TextMsgFactory {
    public static final String DIRECTION_SEND = "SEND";
    public static final String DIRECTION_RECEIVE = "RECEIVE";
    public static final String ADMIN_DRUG = "[\"admin\",\"drug\"]";
    public static final int ONE_SECOND = 1000;

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
        if (s.getType() != -1) {
            result.setBody(s.getBody());
            result.setMessageStatus(s.getData());
            result.setUserData(s.getExtension());
            result.setType(String.valueOf(s.getType()));
            result.setImageHeight(s.getImageHeight());
            result.setImageWidth(s.getImageWidth());
            result.setDuration(s.getDuration());
        }
        return result;
    }

    public static AttachmentData parseAttachment(IMMessage msg) {
        MsgAttachment attachment = msg.getAttachment();
        AttachmentData result = new AttachmentData();
        if (attachment instanceof ImageAttachment) {
            return parseImageData((ImageAttachment) attachment, result);
        } else if (attachment instanceof CustomAttachment) {
            result = parseCustom(msg);
        } else if (attachment instanceof AudioAttachment) {
            result = parseAudio((AudioAttachment) attachment);
        } else if (attachment instanceof VideoAttachment) {
            result.setType(TextMsg.VIDEO);
        } else if (attachment instanceof FileAttachment) {
            result = parseFile((FileAttachment) attachment);
        }

        return result;
    }

    private static AttachmentData parseFile(FileAttachment attachment) {
        AttachmentData result = new AttachmentData();
        result.setBody(attachment.getDisplayName());
        result.setExtension(attachment.getExtension());
        result.setType(TextMsg.FILE);
        result.setData(attachment.getUrl());
        result.setDuration(attachment.getSize());
        return result;
    }

    private static AttachmentData parseAudio(AudioAttachment attachment) {
        AttachmentData result = new AttachmentData();
        long duration = attachment.getDuration() / ONE_SECOND;
        result.setBody(String.valueOf(duration) + "\"");
        result.setType(TextMsg.AUDIO);
        result.setData(attachment.getUrl());
        result.setDuration(duration);
        return result;
    }

    @NonNull
    private static AttachmentData parseImageData(ImageAttachment attachment, AttachmentData result) {
        result.setType(TextMsg.IMAGE);
        result.setData(attachment.getUrl());
        result.setBody("照片");
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
        result.setImageWidth(imageWidth);
        result.setImageHeight(imageHeight);
        return result;
    }

    private static AttachmentData parseCustom(IMMessage msg) {
        CustomAttachment attachment = (CustomAttachment) msg.getAttachment();
        AttachmentData result = new AttachmentData();

        int type = attachment.getType();
//                JSONObject data = object.getJSONObject(KEY_DATA);
        switch (type) {
            case TextMsg.Sticker: {
                result.setBody("贴图");

                StickerAttachment sticker = (StickerAttachment) attachment.getData();
                String text = (StickerManager.FILE_ANDROID_ASSET_STICKER + sticker.getCatalog() + "/" + sticker.getChartlet() + ".png");
//                        textAttachment.setData(text);
                result.setData(text);
                result.setType(TextMsg.Sticker);
                return result;
            }
            case TextMsg.Drug: {
                JSONObject data = (JSONObject) attachment.getData();
//                        textAttachment.setData(text);
                result.setBody(data.toString());
                result.setType(TextMsg.Drug);
                return result;
            }
        }
        return null;
    }
}
