package com.doctor.sun.im;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.avchat.AVChatProfile;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.util.PermissionUtil;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by rick on 13/4/2016.
 */
public class AVChatHandler {
    public static final String TAG = AVChatHandler.class.getSimpleName();

    private static AVChatHandler instance;

    public static AVChatHandler getInstance() {
        if (instance == null) {
            instance = new AVChatHandler();
        }
        return instance;
    }

    public void enableAVChat() {
        registerAVChatIncomingCallObserver(true);
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager instance = AVChatManager.getInstance();
        Observer<AVChatData> observer = new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData chatData) {
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatActivity.launch(AppContext.me(), chatData, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        };
        instance.observeIncomingCall(observer, register);
    }
}
