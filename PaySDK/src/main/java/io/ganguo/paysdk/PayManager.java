package io.ganguo.paysdk;

import android.app.Activity;

import io.ganguo.paysdk.alipay.Alipay;
import io.ganguo.paysdk.wechat.WXPay;


/**
 * 支付模块
 * <p/>
 * Created by Tony on 8/5/15.
 */
public class PayManager {

    /**
     * 支付宝支付
     *
     * @param activity
     */
    public static void alipay(Activity activity, final String orderId, final String totalFee, final String subject, final String body) {
        Alipay.pay(activity, orderId, totalFee, subject, body);
    }

    /**
     * 微信支付
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
    public static void wxpay(Activity activity, String appId, String partnerId, String prepayId, String sign, String nonceStr, String timeStamp, String packageValue) {
        WXPay.pay(activity, appId, partnerId, prepayId, sign, nonceStr, timeStamp, packageValue);
    }

}
