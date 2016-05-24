package com.doctor.sun.im;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.NotificationUtil;
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
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rick on 1/4/2016.
 */
public class NIMConnectionState implements RequestCallback {
    public static final int THIRTY_MINUTES = 1000 * 60 * 30;
    public static final int FIVE_MB = 5242880;
    private static NIMConnectionState instance;
    private statusObserver observer;
    private IMMessageObserver observer1;
    private CustomAttachParser msgAttachmentParser;
    private RequestCallback callback = null;

    public static NIMConnectionState getInstance() {
        if (instance == null) {
            instance = new NIMConnectionState();
        }
        return instance;
    }


    @Override
    public void onSuccess(Object o) {
        observer = new statusObserver();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(observer, true);
        observer1 = new IMMessageObserver();
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(observer1, true);
        service.observeAttachmentProgress(new Observer<AttachmentProgress>() {
            @Override
            public void onEvent(AttachmentProgress attachmentProgress) {
                if (attachmentProgress.getTotal() > FIVE_MB) {
                    if (attachmentProgress.getTransferred() == attachmentProgress.getTotal()) {
                        NotificationUtil.cancelUploadMsg();
                    } else {
                        int transferred = (int) (attachmentProgress.getTransferred() / 1000);
                        int total = (int) (attachmentProgress.getTotal() / 1000);
                        NotificationUtil.showUploadProgress(transferred, total);
                    }
                }
            }
        }, true);
        msgAttachmentParser = new CustomAttachParser();
        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(msgAttachmentParser);
        if (callback != null) {
            callback.onSuccess(o);
            callback = null;
        }
    }

    @Override
    public void onFailed(int i) {
        if (callback != null) {
            callback.onFailed(i);
            callback = null;
        }
    }

    @Override
    public void onException(Throwable throwable) {
        if (callback != null) {
            callback.onException(throwable);
            callback = null;
        }
    }

    public void setCallback(RequestCallback callback) {
        this.callback = callback;
    }

    public boolean isLogin() {
        StatusCode status = NIMClient.getStatus();
        return status.equals(StatusCode.LOGINED);
    }


    public static void saveMsg(IMMessage msg) {
        saveMsg(msg, false);
    }

    public static void saveMsg(final IMMessage msg, final boolean haveRead) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                if (msg1.getType().equals(MsgTypeEnum.avchat.toString())) {
                    msg1.setHaveRead(true);
                } else {
                    msg1.setHaveRead(haveRead);
                    if (!haveRead) {
                        NotificationUtil.showNotification(msg1);
                    }
                }
                realm.copyToRealmOrUpdate(msg1);
            }
        });
    }

    public static void saveMsgs(final List<IMMessage> msgs, final boolean haveRead) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (IMMessage msg : msgs) {
                    TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                    if (msg1.getType().equals(MsgTypeEnum.avchat.toString())) {
                        msg1.setHaveRead(true);
                    } else {
                        msg1.setHaveRead(haveRead);
                    }
                    realm.copyToRealmOrUpdate(msg1);
                }
            }
        });
    }

    private static class IMMessageObserver implements Observer<IMMessage> {
        @Override
        public void onEvent(IMMessage imMessage) {
            if (imMessage.getMsgType().equals(MsgTypeEnum.audio)) {
                if (imMessage.getStatus().equals(MsgStatusEnum.read)) {
                    saveMsg(imMessage, true);
                } else {
                    if (imMessage.getDirect().equals(MsgDirectionEnum.In)) {
                        boolean haveRead = passThirtyMinutes(imMessage);
                        saveMsg(imMessage, haveRead);
                    } else {
                        saveMsg(imMessage, true);
                    }
                }
            } else {
                saveMsg(imMessage, true);
            }
        }
    }

    private static class statusObserver implements Observer<List<IMMessage>> {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            for (IMMessage imMessage : imMessages) {
                boolean haveRead = passThirtyMinutes(imMessage);
                saveMsg(imMessage, haveRead);
            }
        }
    }

    private static boolean passThirtyMinutes(IMMessage imMessage) {
        return System.currentTimeMillis() - imMessage.getTime() > THIRTY_MINUTES;
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
                    case TextMsg.Drug: {
                        CustomAttachment<JSONObject> customAttachment = new CustomAttachment<>();
                        customAttachment.setType(TextMsg.Drug);
                        customAttachment.setData(object.getJSONObject("data"));
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
}
