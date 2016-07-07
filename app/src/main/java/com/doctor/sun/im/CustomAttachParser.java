package com.doctor.sun.im;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.ExtendTimeAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rick on 11/4/2016.
 */
public class CustomAttachParser implements MsgAttachmentParser {
    private static CustomAttachParser instance;

    public static CustomAttachParser getInstance() {
        if (instance == null) {
            instance = new CustomAttachParser();
        }
        return instance;
    }

    private static final String KEY_TYPE = "type";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        try {
            JSONObject object = new JSONObject(json);

            int type = object.getInt(KEY_TYPE);
//                JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
                case TextMsg.Sticker: {
                    JavaType javaType = TypeFactory.defaultInstance()
                            .constructParametricType(CustomAttachment.class, StickerAttachment.class);
                    return JacksonUtils.<CustomAttachment<StickerAttachment>>fromJson(object.toString(), javaType);
                }
                case TextMsg.Drug: {
                    CustomAttachment<JSONObject> customAttachment = new CustomAttachment<>();
                    customAttachment.setType(TextMsg.Drug);
                    customAttachment.setData(object.getJSONObject("data"));
                    return customAttachment;
                }

                case TextMsg.EXTEND_TIME: {
                    CustomAttachment<ExtendTimeAttachment> customAttachment = new CustomAttachment<>();
                    customAttachment.setType(TextMsg.EXTEND_TIME);
                    JSONObject data = object.getJSONObject("data");
                    ExtendTimeAttachment extendTimeAttachment = JacksonUtils.fromJson(data.toString(), ExtendTimeAttachment.class);
                    customAttachment.setData(extendTimeAttachment);
                    return customAttachment;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
