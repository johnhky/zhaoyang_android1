package com.doctor.sun.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.doctor.sun.util.PayCallback;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调
 * Created by Roger on 8/13/15.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;
    private static PayCallback callback;

    public static void setCallback(PayCallback callback) {
        WXPayEntryActivity.callback = callback;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        api = WXAPIFactory.createWXAPI(this, "1282212201");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            if (callback != null) {
                switch (errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        if (callback != null) {
                            callback.onPaySuccess();
                        }
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_COMM:
                        if (callback != null) {
                            callback.onPayFail();
                        }
                        finish();
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        if (callback != null) {
                            callback.onPayFail();
                        }
                        finish();
                        break;
                    default:
                        break;
                }
                callback = null;
            }
        }
    }
}
