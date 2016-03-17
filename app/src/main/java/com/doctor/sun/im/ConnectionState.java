package com.doctor.sun.im;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.SdkErrorCode;

import io.realm.Realm;

/**
 * Created by rick on 30/1/2016.
 */
public class ConnectionState implements ECDevice.OnECDeviceConnectListener {
    private Realm realm;
    private ECDevice.ECConnectState state;
    private ECInitParams.LoginAuthType authType;

    private static ConnectionState instance;

    public static ConnectionState getInstance() {
        if (instance == null) {
            instance = new ConnectionState();
        }
        return instance;
    }

    public void onConnect() {
        // 兼容4.0，5.0可不必处理
    }

    @Override
    public void onDisconnect(ECError error) {
        // 兼容4.0，5.0可不必处理
    }

    @Override
    public void onConnectState(ECDevice.ECConnectState state, ECError error) {
        this.state = state;
        if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (null != realm) {
                realm.close();
            }
            if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                //账号异地登陆
            } else {
                //连接状态失败
                if (authType != null && authType.equals(ECInitParams.LoginAuthType.NORMAL_AUTH)) {
                    setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
                } else {
                    setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
                }
                Messenger.getInstance().logout();
            }
        } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            // 登陆成功
            if (null == realm) {
                realm = Realm.getDefaultInstance();
            }
        }
    }

    public ECDevice.ECConnectState getState() {
        return state;
    }

    public void setState(ECDevice.ECConnectState state) {
        this.state = state;
    }

    public Realm getRealm() {
        return realm;
    }


    public ECInitParams.LoginAuthType getAuthType() {
        return authType;
    }

    public ConnectionState setAuthType(ECInitParams.LoginAuthType authType) {
        this.authType = authType;
        return this;
    }
}
