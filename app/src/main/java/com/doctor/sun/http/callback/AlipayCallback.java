package com.doctor.sun.http.callback;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.doctor.sun.util.PayCallback;

/**
 * Created by rick on 23/1/2016.
 */
public class AlipayCallback extends SimpleCallback<String> {
    private static final String STATUS_SUCCESS = "9000";
    public static final int PAY_FLAG = 1;


    private final Activity activity;
    private static PayCallback mCallback;
    private static PayStatusHandler payStatusHandler = new PayStatusHandler();


    public AlipayCallback(final Activity activity, final AppointMent data) {
        this.activity = activity;
        mCallback = new PayCallback() {

            @Override
            public void onPaySuccess() {
                Intent intent = PaySuccessActivity.makeIntent(activity, data);
                activity.startActivity(intent);
            }

            @Override
            public void onPayFail() {
                Intent intent = PayFailActivity.makeIntent(activity, data, false);
                activity.startActivity(intent);
            }
        };
    }

    public AlipayCallback(final Activity activity, final EmergencyCall data) {
        this.activity = activity;
        mCallback = new PayCallback() {

            @Override
            public void onPaySuccess() {
//                Intent intent = PaySuccessActivity.makeIntent(activity, data);
//                activity.startActivity(intent);
            }

            @Override
            public void onPayFail() {
                Intent intent = PayFailActivity.makeIntent(activity, data, false);
                activity.startActivity(intent);
            }
        };
    }

    public AlipayCallback(final Activity activity, final int money){
        this.activity = activity;
        mCallback  = new PayCallback() {
            @Override
            public void onPaySuccess() {
                Intent intent = PaySuccessActivity.makeIntent(activity);
                activity.startActivity(intent);
            }

            @Override
            public void onPayFail() {
                Intent intent = PayFailActivity.makeIntent(activity,money,"alipay");
                activity.startActivity(intent);
            }
        };
    }

    @Override
    protected void handleResponse(final String data) {
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
                    PayResult aliPayResult = new PayResult(msg.obj.toString());

//                    LOG.d("aliPayResult" + aliPayResult);
                    String payStatus = aliPayResult.getResultStatus();
                    if (payStatus.equals(STATUS_SUCCESS)) {
                        //Log.d("success");
                        mCallback.onPaySuccess();
                    } else {
                        //Log.d("fail" + payStatus);
                        mCallback.onPayFail();
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
