package io.ganguo.paysdk.wechat;

import android.app.Activity;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.paysdk.PayConstants;
import io.ganguo.paysdk.event.PayFailureEvent;

/**
 * 微信支付
 * <p/>
 * Created by Tony on 11/5/14.
 */
public class WXPay {

    /**
     * 微信钱包支付
     *
     * @param activity
     * @param appId
     * @param partnerId
     * @param prepayId
     * @param sign
     * @param nonceStr
     * @param timeStamp
     * @param packageValue
     */
    public static void pay(Activity activity, String appId, String partnerId, String prepayId, String sign, String nonceStr, String timeStamp, String packageValue) {
        final IWXAPI wxApi = WXAPIFactory.createWXAPI(activity, PayConstants.WECHAT_APPID);
        // 检查是否安装微信
        if (!wxApi.isWXAppInstalled()) {
            ToastHelper.showMessage(activity, "支付失败，请先安装微信应用");
            return;
        }
        wxApi.registerApp(PayConstants.WECHAT_APPID);

        PayReq payReq = new PayReq();
        payReq.appId = appId;
        payReq.partnerId = partnerId;
        payReq.prepayId = prepayId;
        payReq.nonceStr = nonceStr;
        payReq.sign = sign;
        payReq.timeStamp = timeStamp;
        payReq.packageValue = packageValue;
        boolean isSendReqSuccess = wxApi.sendReq(payReq);
        if (!isSendReqSuccess) {
            EventHub.post(new PayFailureEvent());
        }
    }

}
