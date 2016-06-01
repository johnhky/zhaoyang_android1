package com.doctor.sun.im;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.emoji.Emoticon;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.SendMessageEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.util.JacksonUtils;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;

import java.io.File;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;


/**
 * Created by rick on 12/8/15.
 */
public class IMManager {
    public static final String TAG = IMManager.class.getSimpleName();
    private static IMManager instance;

    private ImAccount account;
    private ECInitParams params;

    public static IMManager getInstance() {
        if (instance == null) {
            instance = new IMManager();
        }
        return instance;
    }

    public void login() {
        ImAccount account = getVoipAccount();
        if (account != null) {
            IMManager.getInstance().login(account);
        }
    }

    public void login(final ImAccount account) {
        try {
            loginRIM(account);
        } catch (Exception e) {
            logoutRIM();
        }
        try {
            loginNIM(account);
        } catch (Exception e) {
            logoutNIM();
        }
    }

    private void loginNIM(ImAccount account) {
        NIMConnectionState instance = NIMConnectionState.getInstance();

        if (instance.isLogin()) {
            return;
        }

        LoginInfo loginInfo = new LoginInfo(account.getYunxinAccid(), account.getYunxinToken());
        NIMClient.getService(AuthService.class)
                .login(loginInfo)
                .setCallback(instance);
    }

    //登录容联im
    private void loginRIM(ImAccount account) {
        if (isRIMLogin()) {
            return;
        }

        Intent intent = new Intent(AppContext.me(), VoIPCallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);

        ECDevice.setOnDeviceConnectListener(ConnectionState.getInstance());
        ECDevice.setOnChatReceiveListener(OnMessageReceiveListener.getInstance());
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if (setupManager != null) {
            // 目前支持下面三种路径查找方式
            // 1、如果是assets目录则设置为前缀[assets://]
            setupManager.setInComingRingUrl(true, "assets://phonering.mp3");
            setupManager.setOutGoingRingUrl(true, "assets://phonering.mp3");
            setupManager.setBusyRingTone(true, "assets://played.mp3");
            // 2、如果是raw目录则设置为前缀[raw://]
            // 3、如果是SDCard目录则设置为前缀[file://]
        }


        this.account = account;
        if (params == null) {
            params = ECInitParams.createParams();
        }

        params.reset();
        //voip账号+voip密码方式:
        params.setAppKey(BuildConfig.IM_KEY);
        params.setToken(BuildConfig.IM_TOKEN);
        params.setUserid(account.getVoipAccount());
        // 设置登陆验证模式（是否验证密码）PASSWORD_AUTH-密码登录方式
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线:FORCE_LOGIN  默认登录:AUTO）
        if (!account.getVoipPwd().equals("")) {
            params.setPwd(account.getVoipPwd());
        }
        params.setMode(ECInitParams.LoginMode.AUTO);
        ECInitParams.LoginAuthType authType = ConnectionState.getInstance().getAuthType();
        if (authType == ECInitParams.LoginAuthType.NORMAL_AUTH) {
            params.setPwd("");
        } else {
            params.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        }
        params.setAuthType(authType);

        ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();

        if (callInterface != null) {
            callInterface.setOnVoIPCallListener(new VoIPCallback());
        }


//        第三步:验证参数是否正确，注册SDK
        if (params.validate()) {
            // 判断注册参数是否正确
            if (!isRIMLogin()) {
                ECDevice.login(params);
            }
        }
    }

    public void logout() {
        logoutRIM();
        logoutNIM();
    }

    private void logoutRIM() {
        if (isRIMLogin()) {
            ECDevice.logout(new ECDevice.OnLogoutListener() {
                @Override
                public void onLogout() {
                    ConnectionState.getInstance().setState(ECDevice.ECConnectState.CONNECT_FAILED);
                }
            });
        }
    }

    public void logoutNIM() {
        if (isNIMLogin()) {
            NIMClient.getService(AuthService.class).logout();
        }
    }

    public String getMyAccount() {
        return account.getVoipAccount();
    }

    public static ImAccount getVoipAccount() {
        String json = Config.getString(Constants.VOIP_ACCOUNT);
        if (null != json && !json.equals("")) {
            ImAccount account = JacksonUtils.fromJson(json, ImAccount.class);
            return account;
        }
        return null;
    }

    public void sentTextMsg(String to, SessionTypeEnum type, String text) {
        final IMMessage message = MessageBuilder.createTextMessage(
                to, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                type, // 聊天类型，单聊或群组
                text// 文本内容
        );
        sendMsg(message);
    }

    public void sentSticker(String to, SessionTypeEnum type, Emoticon emoticon) {
        CustomAttachment<StickerAttachment> customAttachment = new CustomAttachment<>();
        StickerAttachment msgAttachment = new StickerAttachment();
        msgAttachment.setCatalog(emoticon.getId());
        msgAttachment.setChartlet(emoticon.getTag().replace(".png", ""));
        customAttachment.setType(TextMsg.Sticker);
        customAttachment.setData(msgAttachment);
        final IMMessage message = MessageBuilder.createCustomMessage(to, type, customAttachment);
        sendMsg(message);
    }


    public void sentVideo(String to, SessionTypeEnum type, File image) {
        final IMMessage message = MessageBuilder.createVideoMessage(to, type, image, 0, 0, 0, "");
        sendMsg(message);
    }

    public void sentImage(String to, SessionTypeEnum type, File image) {
        final IMMessage message = MessageBuilder.createImageMessage(to, type, image);
        sendMsg(message);
    }

    public void sentAudio(String to, SessionTypeEnum type, File audio, long time) {
        final IMMessage message = MessageBuilder.createAudioMessage(to, type, audio, time);
        sendMsg(message);
    }

    public void sentFile(String to, SessionTypeEnum type, File file) {
        final IMMessage message = MessageBuilder.createFileMessage(to, type, file, "文件");
        sendMsg(message);
    }

    private void sendMsg(IMMessage message) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = true; // 该消息不计入未读数
        config.enableHistory = true;
        config.enableRoaming = true;
        message.setConfig(config);
        EventHub.post(new SendMessageEvent());

        InvocationFuture<Void> voidInvocationFuture = NIMClient.getService(MsgService.class).sendMessage(message, true);

        voidInvocationFuture.setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {
                Api.of(ToolModule.class).crashLog(TAG + i).enqueue(new SimpleCallback<Void>() {
                    @Override
                    protected void handleResponse(Void response) {

                    }
                });
            }

            @Override
            public void onException(Throwable throwable) {
                Api.of(ToolModule.class).crashLog(TAG + throwable.toString()).enqueue(new SimpleCallback<Void>() {
                    @Override
                    protected void handleResponse(Void response) {

                    }
                });
            }
        });
    }

    public boolean isRIMLogin() {
        return ConnectionState.getInstance().getState() == ECDevice.ECConnectState.CONNECT_SUCCESS;
    }

    public boolean makePhoneCall(String to) {
        ECVoIPCallManager ecVoIPCallManager = ECDevice.getECVoIPCallManager();
        try {
            if (ecVoIPCallManager != null) {
                String mCurrentCallId = ecVoIPCallManager.makeCall(
                        ECVoIPCallManager.CallType.VOICE, to);

                if (null == mCurrentCallId) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "makePhoneCall: " + to);
            return false;
        }
    }

    public boolean isNIMLogin() {
        return NIMConnectionState.getInstance().isLogin();
    }
}
