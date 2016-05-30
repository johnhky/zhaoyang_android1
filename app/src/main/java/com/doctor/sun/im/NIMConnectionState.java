package com.doctor.sun.im;

import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.im.observer.LoginSyncStatusObserver;
import com.doctor.sun.util.NotificationUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import io.realm.Realm;

/**
 * Created by rick on 1/4/2016.
 */
public class NIMConnectionState implements RequestCallback {
    public static final int THIRTY_MINUTES = 1000 * 60 * 30;
    public static final int FIVE_MB = 5242880;
    private static NIMConnectionState instance;
    private RequestCallback callback = null;

    public static NIMConnectionState getInstance() {
        if (instance == null) {
            instance = new NIMConnectionState();
        }
        return instance;
    }


    @Override
    public void onSuccess(Object o) {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new LoginSyncStatusObserver(), true);
        CustomAttachParser msgAttachmentParser = new CustomAttachParser();
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
                if (msg1 == null) return;

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
                    if (msg1 == null) continue;

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

    public static boolean passThirtyMinutes(IMMessage imMessage) {
        return System.currentTimeMillis() - imMessage.getTime() > THIRTY_MINUTES;
    }

}
