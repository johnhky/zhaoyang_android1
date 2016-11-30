package com.doctor.sun.im;

import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.emoji.Emoticon;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.im.MsgHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.SendMessageEvent;
import com.doctor.sun.im.custom.CustomAttachment;
import com.doctor.sun.im.custom.StickerAttachment;
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

import java.io.File;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;


/**
 * Created by rick on 12/8/15.
 */
public class IMManager {
    public static final String TAG = IMManager.class.getSimpleName();
    public static final int RETRY_THREHOLD = 4;
    private static IMManager instance;

    private ImAccount account;
//    private ECInitParams params;

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


    public void logout() {
//        logoutRIM();
        logoutNIM();
    }


    public void logoutNIM() {
        if (isNIMLogin()) {
            NIMClient.getService(AuthService.class).logout();
        }
    }

    public String getMyAccount() {
        if (account == null) {
            return "";
        }
        return account.getVoipAccount();
    }

    public static ImAccount getVoipAccount() {
        String json = Config.getString(Constants.VOIP_ACCOUNT);
        if (null != json && !json.equals("")) {
            return JacksonUtils.fromJson(json, ImAccount.class);
        }
        return null;
    }

    public InvocationFuture<Void> sentTextMsg(String to, SessionTypeEnum type, String text, boolean enablePush) {
        final IMMessage message = MessageBuilder.createTextMessage(
                to, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                type, // 聊天类型，单聊或群组
                text// 文本内容
        );
        return sendMsg(message, enablePush);
    }

    public void sentSticker(String to, SessionTypeEnum type, Emoticon emoticon, boolean enablePush) {
        CustomAttachment<StickerAttachment> customAttachment = new CustomAttachment<>();
        StickerAttachment msgAttachment = new StickerAttachment();
        msgAttachment.setCatalog(emoticon.getId());
        msgAttachment.setChartlet(emoticon.getTag().replace(".png", ""));
        customAttachment.setType(TextMsg.Sticker);
        customAttachment.setData(msgAttachment);
        final IMMessage message = MessageBuilder.createCustomMessage(to, type, customAttachment);
        sendMsg(message, enablePush);
    }


    public void sentVideo(String to, SessionTypeEnum type, File image, boolean enablePush) {
        final IMMessage message = MessageBuilder.createVideoMessage(to, type, image, 0, 0, 0, "");
        sendMsg(message, enablePush);
    }

    public void sentImage(String to, SessionTypeEnum type, File image, boolean enablePush) {
        final IMMessage message = MessageBuilder.createImageMessage(to, type, image);
        sendMsg(message, enablePush);
    }

    public void sentAudio(String to, SessionTypeEnum type, File audio, long time, boolean enablePush) {
        final IMMessage message = MessageBuilder.createAudioMessage(to, type, audio, time);
        sendMsg(message, enablePush);
    }

    public void sentFile(String to, SessionTypeEnum type, File file, boolean enablePush) {
        final IMMessage message = MessageBuilder.createFileMessage(to, type, file, "文件");
        sendMsg(message, enablePush);
    }

    public InvocationFuture<Void> sendMsg(final IMMessage message, final boolean enablePush) {
        return sendMsgImpl(message, enablePush, 0);
    }

    public InvocationFuture<Void> sendMsgImpl(final IMMessage message, final boolean enablePush, final int retryCount) {
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableUnreadCount = true; // 该消息不计入未读数
        config.enableHistory = true;
        config.enableRoaming = true;
        config.enablePush = enablePush;
        message.setConfig(config);
        EventHub.post(new SendMessageEvent());
        MsgHandler.saveMsg(message, true);

        final MsgService service = NIMClient.getService(MsgService.class);
        InvocationFuture<Void> voidInvocationFuture = service.sendMessage(message, true);

        voidInvocationFuture.setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailed(int i) {
                service.saveMessageToLocal(message, false);
                MsgHandler.saveMsg(message);
                Toast.makeText(AppContext.me(), "发送消息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {
                service.saveMessageToLocal(message, false);
                MsgHandler.saveMsg(message);
                Toast.makeText(AppContext.me(), "发送消息失败", Toast.LENGTH_SHORT).show();
            }
        });
        return voidInvocationFuture;
    }

    public boolean isNIMLogin() {
        return NIMConnectionState.getInstance().isLogin();
    }
}
