package io.ganguo.opensdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import io.ganguo.library.util.Strings;

/**
 * Step Builder
 * QQWeibo Share
 * Created by Lynn on 2/19/16.
 */
public final class ShareQQWeiboStepBuilder extends BaseStepBuilder {

    private ShareQQWeiboStepBuilder() {
    }

    public static TitleStep QQWeibo(Context context, PlatformActionListener listener) {
        return new QQWeiboStep(context, listener);
    }

    public interface TitleStep {
        ContentStep setTitle(@NonNull String title);
    }

    public interface ContentStep {
        OptionsStep setContent(@NonNull String content);
    }

    public interface OptionsStep extends OptionalStep {
        OptionsStep setTitleUrl(@NonNull String url);

        OptionsStep setLongitude(float longitude);

        OptionsStep setLatitude(float latitude);

        OptionsStep setImageUrl(@Nullable String imageUrl);

        OptionsStep setImagePath(@Nullable String imagePath);
    }

    private static class QQWeiboStep extends BaseStep implements TitleStep, ContentStep, OptionsStep {
        private QQWeiboStep(Context context, PlatformActionListener listener) {
            super(context, listener);
        }

        /**
         * 分享标题 必填参数 512Bytes以内
         */
        private String title;

        /**
         * 分享内容 必填参数 1KB以内
         */
        private String content;

        /**
         * 标题链接 必填参数 10KB以内
         */
        private String titleUrl;

        /**
         * 分享图片网络地址 可选填参数 10KB以内
         */
        private String imageUrl;

        /**
         * 分享图片本地路径 可选填参数 10M以内
         */
        private String imagePath;

        /**
         * 纬度 -90.0到+90.0 +表示北纬
         */
        private float latitude;

        /**
         * 经度 -180.0到+180.0 +表示东经
         */
        private float longitude;

        @Override
        public OptionsStep setContent(@NonNull String content) {
            this.content = content;
            return this;
        }

        @Override
        public OptionsStep setImagePath(@Nullable String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        @Override
        public OptionsStep setImageUrl(@Nullable String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        @Override
        public OptionsStep setLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        @Override
        public OptionsStep setLongitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        @Override
        public ContentStep setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        @Override
        public OptionsStep setTitleUrl(@NonNull String url) {
            this.titleUrl = url;
            return this;
        }

        @Override
        public ShareStep commit() {
            return this;
        }

        @Override
        public void share() {
            OnekeyShare oks = new OnekeyShare();
            // 分享内容
            if (Strings.isNotEmpty(content)) {
                oks.setText(content);
            }
            // 网络图片地址
            if (Strings.isNotEmpty(imageUrl)) {
                oks.setImageUrl(imageUrl);
            }
            // 本地图片地址
            if (Strings.isNotEmpty(imagePath)) {
                oks.setImagePath(imagePath);
            }

            // 位置信息
            if (latitude != 0 && longitude != 0) {
                oks.setLatitude(latitude);
                oks.setLongitude(longitude);
            }

            // 指定分享到微信好友
            oks.setPlatform(TencentWeibo.NAME);
            oks.setCallback(getListener());
            // 显示编辑页
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }
}
