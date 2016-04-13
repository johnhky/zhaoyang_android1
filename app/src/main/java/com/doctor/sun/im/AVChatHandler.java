package com.doctor.sun.im;

import com.doctor.sun.R;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatRingerConfig;

/**
 * Created by rick on 13/4/2016.
 */
public class AVChatHandler {

    private static AVChatHandler instance;

    public static AVChatHandler getInstance() {
        if (instance == null) {
            instance = new AVChatHandler();
        }
        return instance;
    }

    private void enableAVChat() {
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
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData chatData) {
//                AVChatActivity.launch(DemoCache.getContext(), chatData);
            }
        }, register);
    }
}
