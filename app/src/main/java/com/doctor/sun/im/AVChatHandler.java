package com.doctor.sun.im;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.avchat.AVChatProfile;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatRingerConfig;

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
        setupAVChat();
        registerAVChatIncomingCallObserver(true);
    }

    private void setupAVChat() {
        AVChatManager.getInstance().setRingerConfig(getDefaultConfig()); // 铃声配置
    }

    public static AVChatRingerConfig getDefaultConfig() {
        AVChatRingerConfig config = new AVChatRingerConfig();
        config.res_connecting = R.raw.avchat_connecting;
        config.res_no_response = R.raw.avchat_no_response;
        config.res_peer_busy = R.raw.avchat_peer_busy;
        config.res_peer_reject = R.raw.avchat_peer_reject;
        config.res_ring = R.raw.avchat_ring;
        return config;
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
