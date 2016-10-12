package com.doctor.sun.http.callback;

import android.app.Activity;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.WeChatPayDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.event.PayFailEvent;
import com.doctor.sun.event.PaySuccessEvent;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by rick on 23/1/2016.
 */
public class WeChatPayCallback extends SimpleCallback<WeChatPayDTO> {
    private final PayCallback mCallback;
    private Activity activity;

    public WeChatPayCallback(final Activity activity, final Appointment data) {
        this.activity = activity;
        mCallback = new PayCallback() {

            @Override
            public void onPaySuccess() {
                EventHub.post(new PaySuccessEvent(data));
            }

            @Override
            public void onPayFail() {
                EventHub.post(new PayFailEvent(data, false));
            }
        };
        WXPayEntryActivity.setCallback(mCallback);
    }

//    public WeChatPayCallback(final Activity activity, final UrgentCall data) {
//        this.activity = activity;
//        mCallback = new PayCallback() {
//
//            @Override
//            public void onPaySuccess() {
//                Intent intent = PaySuccessActivity.makeIntent(activity, data);
//                activity.startActivity(intent);
//            }
//
//            @Override
//            public void onPayFail() {
//                Intent intent = PayFailActivity.makeIntent(activity, data, true);
//                activity.startActivity(intent);
//            }
//        };
//        WXPayEntryActivity.setCallback(mCallback);
//    }

    public WeChatPayCallback(final Activity activity, final String money, final HashMap<String, String> extraField) {
        this.activity = activity;
        mCallback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                EventHub.post(new PaySuccessEvent());
            }

            @Override
            public void onPayFail() {
                EventHub.post(new PayFailEvent(money, false, extraField));
            }
        };
        WXPayEntryActivity.setCallback(mCallback);
    }

    @Override
    protected void handleResponse(WeChatPayDTO response) {
        PayReq req = new PayReq();

        req.appId = response.getAppid();
        req.partnerId = response.getPartnerid();
        req.prepayId = response.getPrepayid();
        req.packageValue = response.getPackageX();
        req.nonceStr = response.getNoncestr();
        req.timeStamp = response.getTimestamp();
        req.sign = response.getSign();

        IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, response.getAppid());
        msgApi.registerApp(response.getAppid());
        msgApi.sendReq(req);
    }

    @Override
    public void onFailure(Call<ApiDTO<WeChatPayDTO>> call, Throwable t) {
        super.onFailure(call, t);
        String localizedMessage = t.getLocalizedMessage();
        if (localizedMessage != null && localizedMessage.contains("finish_pay")) {
            mCallback.onPaySuccess();
        }
    }
}
