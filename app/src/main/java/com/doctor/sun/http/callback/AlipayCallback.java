package com.doctor.sun.http.callback;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.doctor.sun.event.PayFailEvent;
import com.doctor.sun.event.PaySuccessEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.util.PayCallback;

import java.util.HashMap;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 23/1/2016.
 */
public class AlipayCallback extends SimpleCallback<String> {
    private static final String STATUS_SUCCESS = "9000";
    public static final int PAY_FLAG = 1;


    private Activity activity;
    private static PayCallback mCallback;
    private static PayStatusHandler payStatusHandler = new PayStatusHandler();


    public AlipayCallback(final Activity activity, final Appointment data) {
        this.activity = activity;
        mCallback = new PayCallback() {

            @Override
            public void onPaySuccess() {
                LoadingHelper.hideMaterLoading();
                EventHub.post(new PaySuccessEvent(data));
//                Intent intent = PaySuccessActivity.makeIntent(activity, data);
//                activity.startActivity(intent);
            }

            @Override
            public void onPayFail() {
                LoadingHelper.hideMaterLoading();
                EventHub.post(new PayFailEvent(data, false));
//                Intent intent = PayFailActivity.makeIntent(activity, data, false);
//                activity.startActivity(intent);
            }
        };
    }
//

    public AlipayCallback(final Activity activity, final double money, final HashMap<String, String> extraField) {
        this.activity = activity;
        mCallback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                LoadingHelper.hideMaterLoading();
                EventHub.post(new PaySuccessEvent(null));
            }

            @Override
            public void onPayFail() {
                LoadingHelper.hideMaterLoading();
                EventHub.post(new PayFailEvent(money, false, extraField));
//                Intent intent = PayFailActivity.makeIntent(activity, money, false, extraField);
//                activity.startActivity(intent);
            }
        };
    }

    @Override
    protected void handleResponse(final String data) {

        if (data.equals("finish_pay")) {
            if (mCallback != null) {
                mCallback.onPaySuccess();
            }
            return;
        }

        if (data != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    PayTask payTask = new PayTask(activity);
                    String result = payTask.pay(data);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    payStatusHandler.sendMessage(msg);
                }
            }).start();
        }
    }


    /**
     * 处理支付结果回调
     */
    public static class PayStatusHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PAY_FLAG:
                    LoadingHelper.hideMaterLoading();
                    PayResult aliPayResult = new PayResult(msg.obj.toString());

//                    LOG.d("aliPayResult" + aliPayResult);
                    String payStatus = aliPayResult.getResultStatus();
                    if (payStatus.equals(STATUS_SUCCESS)) {
                        //Log.d("success");
                        if (mCallback != null) {
                            mCallback.onPaySuccess();
                        }
                    } else {
                        //Log.d("fail" + payStatus);
                        if (mCallback != null) {
                            mCallback.onPayFail();
                        }
                    }
                    mCallback = null;
            }
        }
    }

    public static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(String rawResult) {

            if (TextUtils.isEmpty(rawResult))
                return;

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        private String gatValue(String content, String key) {
            String prefix = key + "={";
            return content.substring(content.indexOf(prefix) + prefix.length(),
                    content.lastIndexOf("}"));
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

}
