package com.doctor.sun.im;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.util.List;

/**
 * Created by rick on 30/1/2016.
 */
public class OnMessageReceiveListener implements OnChatReceiveListener, ECDevice.OnECDeviceConnectListener {
    public static final int NEW_MSG = 100;
    /**
     * onChatReceiveListener
     */

    private static OnMessageReceiveListener instance;

    public static OnMessageReceiveListener getInstance() {
        if (instance == null) {
            instance = new OnMessageReceiveListener();
        }
        return instance;
    }

    @Override
    public void OnReceivedMessage(ECMessage msg) {
//        // 收到新消息
//        if (msg.getType() == ECMessage.Type.TXT) {
//            Realm realm = ConnectionState.getInstance().getRealm();
//            realm.beginTransaction();
//            TextMsg msg1 = TextMsgFactory.fromECMessage(msg);
//            msg1.setHaveRead(false);
//            realm.copyToRealmOrUpdate(msg1);
//            NotificationUtil.showNotification(msg1);
//            realm.commitTransaction();
//        }
    }


    @Override
    public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {

    }

    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
        // 收到群组通知消息（有人加入、退出...）
        // 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
    }

    @Override
    public void onOfflineMessageCount(int count) {
        // 登陆成功之后SDK回调该接口通知账号离线消息数
    }

    @Override
    public int onGetOfflineMessage() {
        return 10;
    }

    @Override
    public void onReceiveOfflineMessage(List<ECMessage> msgs) {
        // SDK根据应用设置的离线消息拉去规则通知应用离线消息
        for (ECMessage msg : msgs) {
            OnReceivedMessage(msg);
        }
    }

    @Override
    public void onReceiveOfflineMessageCompletion() {
        // SDK通知应用离线消息拉取完成
    }

    @Override
    public void onServicePersonVersion(int version) {
        // SDK通知应用当前账号的个人信息版本号
    }

    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {

    }

    @Override
    public void onSoftVersion(String s, int i) {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect(ECError ecError) {

    }

    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {

    }
}
