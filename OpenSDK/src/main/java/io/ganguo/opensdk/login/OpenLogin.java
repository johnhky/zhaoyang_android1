package io.ganguo.opensdk.login;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 第三方登录
 * <p/>
 * Created by Tony on 10/24/15.
 */
public class OpenLogin {

    /**
     * 通过QQ登录
     */
    public static void loginQQ(Context context, PlatformActionListener listener) {
        ShareSDK.removeCookieOnAuthorize(true);
        Platform platform = ShareSDK.getPlatform(context, QQ.NAME);
        platform.setPlatformActionListener(listener);
        platform.removeAccount(true);
        platform.SSOSetting(false);
        platform.authorize();
    }

    /**
     * 通过微信登录
     */
    public static void loginWechat(Context context, PlatformActionListener listener) {
        ShareSDK.removeCookieOnAuthorize(true);
        Platform platform = ShareSDK.getPlatform(context, Wechat.NAME);
        platform.setPlatformActionListener(listener);
        platform.removeAccount(true);
        platform.SSOSetting(false);
        platform.authorize();
    }

    /**
     * 通过微博登录
     */
    public static void loginSinaWeibo(Context context, PlatformActionListener listener) {
        ShareSDK.removeCookieOnAuthorize(true);
        Platform platform = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        platform.setPlatformActionListener(listener);
        platform.removeAccount(true);
        platform.SSOSetting(false);
        platform.authorize();
    }

}
