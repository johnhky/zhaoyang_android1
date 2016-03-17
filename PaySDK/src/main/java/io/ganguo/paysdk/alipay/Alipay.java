package io.ganguo.paysdk.alipay;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import io.ganguo.paysdk.PayConstants;
import io.ganguo.paysdk.event.PayFailureEvent;
import io.ganguo.paysdk.event.PaySuccessEvent;

/**
 * 支付宝支付
 * <p/>
 * Created by Tony on 8/5/15.
 */
public class Alipay {
    private static Logger LOG = LoggerFactory.getLogger(Alipay.class);

    /**
     * 支付宝支付成功
     */
    private static final String STATUS_SUCCESS = "9000";

    /**
     * 通过客户端进行支付
     *
     * @param activity
     */
    public static void pay(final Activity activity, final String orderId, final String totalFee, final String subject, final String body) {
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                String orderInfo = buildPayInfo(orderId, totalFee, subject, body);
                // 构造 PayTask 对象
                PayTask payTask = new PayTask(activity);
                // 检查账户信息是否完整
                if (!payTask.checkAccountIfExist()) {
                    EventHub.post(new PayFailureEvent());
                }
                // 调用支付接口，获取支付结果
                String result = payTask.pay(orderInfo);
                if (Strings.isEquals(result, STATUS_SUCCESS)) {
                    EventHub.post(new PaySuccessEvent());
                } else {
                    EventHub.post(new PayFailureEvent());
                }
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 构建订单信息
     * 以及取款订单信息进行签名
     *
     * @param orderId
     * @param totalFee
     * @return
     */
    private static String buildPayInfo(String orderId, String totalFee, String subject, String body) {
        // alipay支付信息
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(PayConstants.ALIPAY_PARTNER);
        // 订单单号
        sb.append("\"&out_trade_no=\"");
        sb.append(orderId);
        // 订单标题
        sb.append("\"&subject=\"");
        sb.append(subject);
        // 订单描述
        sb.append("\"&body=\"");
        sb.append(body);
        // 订单金额
        sb.append("\"&total_fee=\"");
        sb.append(totalFee);

        // 支付宝后台通知网址，网址需要做URL编码
        sb.append("\"&notify_url=\"");
        try {
            sb.append(URLEncoder.encode(PayConstants.ALIPAY_NOTIFY_URL, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&return_url=\"");
        sb.append(URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"1");
        sb.append("\"&_input_charset=\"UTF-8");

        // 收款支付宝账号
        sb.append("\"&seller_id=\"");
        sb.append(PayConstants.ALIPAY_SELLER);
        sb.append("\"");

        // 对订单信息进行签名
        String sign = null;
        try {
            sign = URLEncoder.encode(SignUtils.sign(sb.toString(), PayConstants.ALIPAY_PRIVATE_KEY), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append("&sign=\"");
        sb.append(sign);
        // 获取签名方式
        sb.append("\"&sign_type=\"RSA\"");
        return sb.toString();
    }

}
