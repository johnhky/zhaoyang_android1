package io.ganguo.paysdk.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import io.ganguo.paysdk.PayConstants;
import io.ganguo.paysdk.event.PayFailureEvent;
import io.ganguo.paysdk.event.PaySuccessEvent;

/**
 * 微信支付回调
 * 配置：app包名.wxapi.WXPayEntryActivity
 * <p/>
 * Created by Roger on 8/13/15.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        api = WXAPIFactory.createWXAPI(this, PayConstants.WECHAT_APPID);
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
        logger.d("baseReq_" + baseReq);
        logger.d("baseReq_openId_" + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            logger.d("errCode_" + errCode);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    EventHub.post(new PaySuccessEvent());
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    EventHub.post(new PayFailureEvent());
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    EventHub.post(new PayFailureEvent());
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
