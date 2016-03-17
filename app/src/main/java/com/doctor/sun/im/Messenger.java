package com.doctor.sun.im;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.VoipAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.util.JacksonUtils;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import io.ganguo.library.Config;
import io.realm.Realm;


/**
 * Created by rick on 12/8/15.
 */
public class Messenger {
    public static final String TAG = Messenger.class.getSimpleName();
    private static Messenger instance;

    private VoipAccount account;
    private Realm realm;
    private ECInitParams params;

    public static Messenger getInstance() {
        if (instance == null) {
            instance = new Messenger();
        }
        return instance;
    }

    public void login() {
        VoipAccount account = getVoipAccount();
        if (account != null) {
            try {
                Messenger.getInstance().login(account);
            } catch (Exception e) {
                Messenger.getInstance().logout();
            }
        }
    }

    public void login(final VoipAccount account) {
        if (isLogin()) {
            return;
        }

        Intent intent = new Intent(AppContext.me(), VoIPCallActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.me(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);

        ECDevice.setOnDeviceConnectListener(ConnectionState.getInstance());
        ECDevice.setOnChatReceiveListener(OnMessageReceiveListener.getInstance());


        this.account = account;
        if (params == null) {
            params = ECInitParams.createParams();
        }

        params.reset();
        //voip账号+voip密码方式：
        params.setAppKey(BuildConfig.IM_KEY);
        params.setToken(BuildConfig.IM_TOKEN);
        params.setUserid(account.getVoipAccount());
        // 设置登陆验证模式（是否验证密码）PASSWORD_AUTH-密码登录方式
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
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


//        第三步：验证参数是否正确，注册SDK
        if (params.validate()) {
            // 判断注册参数是否正确
            if (!isLogin()) {
                ECDevice.login(params);
            }
        }

    }

    public void logout() {
        if (isLogin()) {
            ECDevice.logout(new ECDevice.OnLogoutListener() {
                @Override
                public void onLogout() {
                    ConnectionState.getInstance().setState(ECDevice.ECConnectState.CONNECT_FAILED);
                }
            });
        }
    }

    public String getMyAccount() {
        return account.getVoipAccount();
    }

    public static VoipAccount getVoipAccount() {
        String json = Config.getString(Constants.VOIP_ACCOUNT);
        if (null != json && !json.equals("")) {
            VoipAccount account = JacksonUtils.fromJson(json, VoipAccount.class);
            return account;
        }
        return null;
    }

    public void sentTextMsg(String to, String userData, String text) {
        if (!isLogin()) return;

        try {
            // 组建一个待发送的ECMessage
            ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
            //设置消息的属性：发出者，接受者，发送时间等
            msg.setForm(getMyAccount());
            msg.setMsgTime(System.currentTimeMillis());

            //如果需要跨应用发送消息，需通过appkey+英文井号+用户帐号的方式拼接，发送录音、发送群组消息等与此方式一致。
            //例如：appkey=20150314000000110000000000000010
            //msg.setTo(""$appkey#$John的账号");
            // 设置消息接收者
            msg.setTo(to);
            msg.setSessionId(to);
            // 设置消息发送类型（发送或者接收）
            msg.setDirection(ECMessage.Direction.SEND);
            msg.setUserData(userData);


            // 创建一个文本消息体，并添加到消息对象中
            ECTextMessageBody msgBody = new ECTextMessageBody(text);


            // 将消息体存放到ECMessage中
            msg.setBody(msgBody);
            // 调用SDK发送接口发送消息到服务器
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                @Override
                public void onSendMessageComplete(ECError error, ECMessage message) {
                    // 处理消息发送结果
                    Log.e(TAG, "onSendMessageComplete: " + error.errorMsg);
                    if (message == null) {
                        return;
                    }
                    // 将发送的消息更新到本地数据库并刷新UI
                    if (message.getType() == ECMessage.Type.TXT) {
                        if (null == realm || realm.isClosed()) {
                            realm = Realm.getDefaultInstance();
                        }
                        realm.beginTransaction();
                        TextMsg msg1 = TextMsg.fromECMessage(message);
                        msg1.setHaveRead(true);
                        realm.copyToRealmOrUpdate(msg1);
                        realm.commitTransaction();
                    }
                }

                @Override
                public void onProgress(String msgId, int totalByte, int progressByte) {
                    // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                }
            });
        } catch (Exception e) {
            // 处理发送异常
            Log.e("ECSDK_Demo", "send message fail , e=" + e.getMessage());
            logout();
        }

    }

    public boolean isLogin() {
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
}
