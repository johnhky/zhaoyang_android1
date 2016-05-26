package com.doctor.sun.im.observer;

import com.doctor.sun.im.IMManager;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;

/**
 * Created by rick on 26/5/2016.
 */
public class LoginSyncStatusObserver implements Observer<LoginSyncStatus> {
    @Override
    public void onEvent(LoginSyncStatus status) {
        if (status == LoginSyncStatus.BEGIN_SYNC) {
        } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
        } else {
            IMManager.getInstance().login();
        }
    }
}
