package com.doctor.sun.im.observer;

import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.util.NotificationUtil;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;

/**
 * Created by rick on 26/5/2016.
 */
public class AttachmentProgressObserver implements Observer<AttachmentProgress> {
    @Override
    public void onEvent(AttachmentProgress attachmentProgress) {
        if (attachmentProgress.getTotal() > NIMConnectionState.FIVE_MB) {
            if (attachmentProgress.getTransferred() == attachmentProgress.getTotal()) {
                NotificationUtil.cancelUploadMsg();
            } else {
                int transferred = (int) (attachmentProgress.getTransferred() / 1000);
                int total = (int) (attachmentProgress.getTotal() / 1000);
                NotificationUtil.showUploadProgress(transferred, total);
            }
        }
    }
}
