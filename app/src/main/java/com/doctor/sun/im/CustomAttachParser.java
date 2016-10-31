package com.doctor.sun.im;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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

            String type = object.getString(KEY_TYPE);
//                JSONObject data = object.getJSONObject(KEY_DATA);
            switch (type) {
//                case TextMsg.Sticker: {
                case "3": {
                    JavaType javaType = TypeFactory.defaultInstance()
                            .constructParametricType(CustomAttachment.class, StickerAttachment.class);
                    return JacksonUtils.<CustomAttachment<StickerAttachment>>fromJson(object.toString(), javaType);
                }
//                case String.valueOf(TextMsg.Drug): {
                case "99": {
                    CustomAttachment<JSONObject> customAttachment = new CustomAttachment<>();
                    customAttachment.setType(TextMsg.Drug);
                    customAttachment.setData(object.getJSONObject("data"));
                    return customAttachment;
                }

                case "101": {
                    CustomAttachment<JSONObject> customAttachment = new CustomAttachment<>();
                    customAttachment.setType(TextMsg.DrugV2);
                    customAttachment.setData(object.getJSONObject("data"));
                    return customAttachment;
                }

                case "follow_up_start":
                case "follow_up_end":
                case "appointment_start":
                case "appointment_end": {
                    CustomAttachment<ArrayList<AttachmentPair>> customAttachment = new CustomAttachment<>();
                    customAttachment.setType(TextMsg.REFRESH_APPOINTMENT);
                    ArrayList<AttachmentPair> pairs = new ArrayList<>();
                    Iterator<String> keys = object.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        pairs.add(new AttachmentPair(next, object.getString(next)));
                    }
                    customAttachment.setData(pairs);
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
