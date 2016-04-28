package com.doctor.sun.im;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rick on 1/4/2016.
 */
public class NIMConnectionState implements RequestCallback {
    private static NIMConnectionState instance;

    public static NIMConnectionState getInstance() {
        if (instance == null) {
            instance = new NIMConnectionState();
        }
        return instance;
    }


    @Override
    public void onSuccess(Object o) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(new statusObserver(), true);
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(new IMMessageObserver(), true);
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
    }

    @Override
    public void onFailed(int i) {
    }

    @Override
    public void onException(Throwable throwable) {
    }

    public boolean isLogin() {
        StatusCode status = NIMClient.getStatus();
        return status.equals(StatusCode.LOGINED);
    }


    public static void saveMsg(IMMessage msg) {
        saveMsg(msg, false);
    }

    public static void saveMsg(IMMessage msg, boolean haveRead) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
        msg1.setHaveRead(haveRead);
        realm.copyToRealmOrUpdate(msg1);
        realm.commitTransaction();
        realm.close();
    }

    private static class IMMessageObserver implements Observer<IMMessage> {
        @Override
        public void onEvent(IMMessage imMessage) {
            if (imMessage.getMsgType().equals(MsgTypeEnum.audio)) {
                saveMsg(imMessage, false);
            } else {
                saveMsg(imMessage, true);
            }
        }
    }

    private static class statusObserver implements Observer<List<IMMessage>> {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            for (IMMessage msg : imMessages) {
                saveMsg(msg);
            }
        }
    }

    /**
     * Created by rick on 11/4/2016.
     */
    public static class CustomAttachParser implements MsgAttachmentParser {
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }
}
