package com.doctor.sun.im;

import android.util.Log;

import com.doctor.sun.entity.im.TextMsg;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.RecentContact;

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
    }

    @Override
    public void onFailed(int i) {
    }

    @Override
    public void onException(Throwable throwable) {
    }

    public boolean isConnected() {
        StatusCode status = NIMClient.getStatus();
        return status.equals(StatusCode.LOGINED);
    }


    public static void saveMsg(IMMessage msg) {
        saveMsg(msg, false);
    }

    public static void saveMsg(IMMessage msg, boolean haveRead) {
        if (msg.getMsgType() == MsgTypeEnum.text) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            TextMsg msg1 = TextMsg.fromYXMessage(msg);
            msg1.setHaveRead(haveRead);
            realm.copyToRealmOrUpdate(msg1);
            realm.commitTransaction();
            realm.close();
        }
    }

    private static class IMMessageObserver implements Observer<IMMessage> {
        @Override
        public void onEvent(IMMessage imMessage) {
            saveMsg(imMessage, true);
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

}
