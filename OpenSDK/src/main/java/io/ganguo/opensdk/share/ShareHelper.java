package io.ganguo.opensdk.share;

import android.content.Context;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.ganguo.library.util.Strings;
import io.ganguo.opensdk.bean.QQShare;
import io.ganguo.opensdk.bean.QQWeiboShare;
import io.ganguo.opensdk.bean.QQZoneShare;
import io.ganguo.opensdk.bean.SinaWeiboShare;
import io.ganguo.opensdk.bean.WechatShare;

/**
 * 分享工具
 * <p/>
 * Created by aaron on 10/21/15.
 */
public class ShareHelper {

    /**
     * 分享到QQ
     *
     * @param context
     * @param shareInfo
     */
    public static void shareQQ(Context context, QQShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享标题
        String title = shareInfo.getTitle();
        if (Strings.isNotEmpty(title)) {
            if (title.length() > 30) {
                oks.setTitle(title.substring(0, 25) + "…");
            } else {
                oks.setTitle(title);
            }
        }
        // 分享标题链接
        String titleUrl = shareInfo.getTitleUrl();
        if (Strings.isNotEmpty(titleUrl)) {
            oks.setTitleUrl(titleUrl);
        }
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            if (content.length() > 40) {
                oks.setText(content.substring(0, 34) + "…");
            } else {
                oks.setText(content);
            }
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 图片的本地路径
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }

        // 分享到QQ
        oks.setPlatform(QQ.NAME);
        oks.setCallback(listener);
        // 显示编辑页
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享到QQ空间
     *
     * @param context
     * @param shareInfo
     */
    public static void shareQQZone(Context context, QQZoneShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享标题
        String title = shareInfo.getTitle();
        if (Strings.isNotEmpty(title)) {
            if (title.length() > 30) {
                oks.setTitle(title.substring(0, 25) + "…");
            } else {
                oks.setTitle(title);
            }
        }
        // 分享标题链接
        String titleUrl = shareInfo.getTitleUrl();
        if (Strings.isNotEmpty(titleUrl)) {
            oks.setTitleUrl(titleUrl);
        }
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            if (content.length() > 40) {
                oks.setText(content.substring(0, 34) + "…");
            } else {
                oks.setText(content);
            }
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 本地图片路径
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }
        // 网站名称
        String siteName = shareInfo.getSiteName();
        if (Strings.isNotEmpty(siteName)) {
            oks.setSite(siteName);
        }
        // 网站地址
        String siteUrl = shareInfo.getSiteUrl();
        if (Strings.isNotEmpty(siteUrl)) {
            oks.setSiteUrl(siteUrl);
        }

        // 分享到QZone
        oks.setPlatform(QZone.NAME);
        oks.setCallback(listener);
        // 显示编辑页
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享到新浪微博
     *
     * @param context
     * @param shareInfo
     */
    public static void shareQQWeibo(Context context, QQWeiboShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            oks.setText(content);
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 本地图片地址
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }

        // 位置信息
        if (shareInfo.getLatitude() != 0 && shareInfo.getLongitude() != 0) {
            oks.setLatitude(shareInfo.getLatitude());
            oks.setLongitude(shareInfo.getLongitude());
        }

        // 指定分享到微信好友
        oks.setPlatform(TencentWeibo.NAME);
        oks.setCallback(listener);
        // 显示编辑页
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享到微信好友
     *
     * @param context
     * @param shareInfo
     */
    public static void shareWechat(Context context, WechatShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享标题
        String title = shareInfo.getTitle();
        if (Strings.isNotEmpty(title)) {
            oks.setTitle(title);
        }
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            oks.setText(content);
        }
        // 分享标题链接
        String titleUrl = shareInfo.getTitleUrl();
        if (Strings.isNotEmpty(titleUrl)) {
            oks.setUrl(titleUrl);
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 本地图片地址
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }
        // 分享文件地址
        String filePath = shareInfo.getFilePath();
        if (Strings.isNotEmpty(filePath)) {
            oks.setFilePath(filePath);
        }
        // 分享音乐链接
        String musicUrl = shareInfo.getMusicUrl();
        if (Strings.isNotEmpty(musicUrl)) {
            oks.setMusicUrl(musicUrl);
        }

        // 指定分享到微信好友
        oks.setPlatform(Wechat.NAME);
        oks.setCallback(listener);
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param context
     * @param shareInfo
     */
    public static void shareWechatMoments(Context context, WechatShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享标题
        String title = shareInfo.getTitle();
        if (Strings.isNotEmpty(title)) {
            oks.setTitle(title);
        }
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            oks.setText(content);
        }
        // 分享标题链接
        String titleUrl = shareInfo.getTitleUrl();
        if (Strings.isNotEmpty(titleUrl)) {
            oks.setUrl(titleUrl);
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 本地图片地址
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }
        // 分享文件地址
        String filePath = shareInfo.getFilePath();
        if (Strings.isNotEmpty(filePath)) {
            oks.setFilePath(filePath);
        }
        // 分享音乐链接
        String musicUrl = shareInfo.getMusicUrl();
        if (Strings.isNotEmpty(musicUrl)) {
            oks.setMusicUrl(musicUrl);
        }

        // 指定分享到微信好友
        oks.setPlatform(WechatMoments.NAME);
        oks.setCallback(listener);
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

    /**
     * 分享到新浪微博
     *
     * @param context
     * @param shareInfo
     */
    public static void shareSinaWeibo(Context context, SinaWeiboShare shareInfo, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        // 分享内容
        String content = shareInfo.getContent();
        if (Strings.isNotEmpty(content)) {
            oks.setText(content);
        }
        // 网络图片地址
        String imageUrl = shareInfo.getImageUrl();
        if (Strings.isNotEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);
        }
        // 本地图片地址
        String imagePath = shareInfo.getImagePath();
        if (Strings.isNotEmpty(imagePath)) {
            oks.setImagePath(imagePath);
        }

        // 位置信息
        if (shareInfo.getLatitude() != 0 && shareInfo.getLongitude() != 0) {
            oks.setLatitude(shareInfo.getLatitude());
            oks.setLongitude(shareInfo.getLongitude());
        }

        // 指定分享到微信好友
        oks.setPlatform(SinaWeibo.NAME);
        oks.setCallback(listener);
        // 显示编辑页
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(context);
    }

}
