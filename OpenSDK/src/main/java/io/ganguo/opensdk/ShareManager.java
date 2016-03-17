package io.ganguo.opensdk;

import android.content.Context;

import cn.sharesdk.framework.PlatformActionListener;

/**
 * 管理分享功能
 * Created by Lynn on 2/19/16.
 */
public class ShareManager {
    private Context context;
    private PlatformActionListener listener;

    public ShareManager(Context context, PlatformActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * 分享到QQ
     * @return
     */
    public ShareQQStepBuilder.TitleStep shareQQ() {
        return ShareQQStepBuilder.QQ(context, listener);
    }

    /**
     * 分享到QQ微博
     * @return
     */
    public ShareQQWeiboStepBuilder.TitleStep shareQQWeibo() {
        return ShareQQWeiboStepBuilder.QQWeibo(context, listener);
    }

    /**
     * 分享到QQZone
     * @return
     */
    public ShareQQZoneStepBuilder.TitleStep shareQQZone() {
        return ShareQQZoneStepBuilder.QQZone(context, listener);
    }

    /**
     * 分享到新浪微博
     * @return
     */
    public ShareSinaWeiboStep.ContentStep shareSinaWeibo() {
        return ShareSinaWeiboStep.SinaWeibo(context, listener);
    }

    /**
     * 分享到微信
     * @return
     */
    public ShareWechatStepBuilder.TitleStep shareWechat() {
        return ShareWechatStepBuilder.Wechat(context, listener);
    }

    /**
     * 分享到微信朋友圈
     * @return
     */
    public ShareWechatStepBuilder.TitleStep shareWechatMoments() {
        return ShareWechatStepBuilder.WechatMoments(context, listener);
    }
}
