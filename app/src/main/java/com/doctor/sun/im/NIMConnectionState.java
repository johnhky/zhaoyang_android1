package com.doctor.sun.im;

import com.doctor.sun.im.observer.LoginSyncStatusObserver;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.model.IMMessage;

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


    public static boolean passThirtyMinutes(IMMessage imMessage) {
        return System.currentTimeMillis() - imMessage.getTime() > THIRTY_MINUTES;
    }

}
