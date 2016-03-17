package io.ganguo.opensdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.ganguo.library.util.Strings;

/**
 * Step Builder
 * 微信
 * Created by Lynn on 2/19/16.
 */
public final class ShareWechatStepBuilder extends BaseStepBuilder {
    private ShareWechatStepBuilder() {
    }

    public static TitleStep Wechat(Context context, PlatformActionListener listener) {
        return new WechatStep(context, listener);
    }

    public static TitleStep WechatMoments(Context context, PlatformActionListener listener) {
        return new WechatMomentsStep(context, listener);
    }

    public interface TitleStep {
        ContentStep setTitle(@NonNull String name);
    }

    public interface ContentStep {
        OptionsStep setContent(@NonNull String content);
    }

    public interface OptionsStep extends OptionalStep {
        OptionsStep setTitleUrl(@Nullable String url);

        OptionsStep setImageUrl(@Nullable String imageUrl);

        OptionsStep setFilePath(@Nullable String filePath);

        OptionsStep setMusicUrl(@Nullable String url);

        OptionsStep setImagePath(@Nullable String imagePath);
    }

    private abstract static class WechatBaseStep extends BaseStep implements TitleStep, ContentStep,
            OptionsStep {
        /**
         * 分享标题 必填参数 512Bytes以内
         */
        protected String title;

        /**
         * 分享内容 必填参数 1KB以内
         */
        protected String content;

        /**
         * 标题链接 必填参数 10KB以内
         */
        protected String titleUrl;

        /**
         * 分享图片网络地址 可选填参数 10KB以内
         */
        protected String imageUrl;

        /**
         * 分享图片本地路径 可选填参数 10M以内
         */
        protected String imagePath;

        /**
         * 分享文件路径
         */
        protected String filePath;

        /**
         * 分享音乐链接
         */
        protected String musicUrl;

        public WechatBaseStep(Context context, PlatformActionListener platformActionListener) {
            super(context, platformActionListener);
        }

        @Override
        public OptionsStep setContent(@NonNull String content) {
            this.content = content;
            return this;
        }

        @Override
        public OptionsStep setImageUrl(@Nullable String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        @Override
        public OptionsStep setFilePath(@Nullable String filePath) {
            this.filePath = filePath;
            return this;
        }

        @Override
        public OptionsStep setMusicUrl(@Nullable String url) {
            this.musicUrl = url;
            return this;
        }

        @Override
        public OptionsStep setImagePath(@Nullable String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        @Override
        public ShareStep commit() {
            return this;
        }

        @Override
        public ContentStep setTitle(@NonNull String name) {
            this.title = name;
            return this;
        }

        @Override
        public OptionsStep setTitleUrl(@NonNull String url) {
            this.titleUrl = url;
            return this;
        }
    }

    private static class WechatStep extends WechatBaseStep {

        private WechatStep(Context context, PlatformActionListener listener) {
            super(context, listener);
        }

        @Override
        public void share() {
            OnekeyShare oks = new OnekeyShare();
            // 分享标题
            if (Strings.isNotEmpty(title)) {
                oks.setTitle(title);
            }
            // 分享内容
            if (Strings.isNotEmpty(content)) {
                oks.setText(content);
            }
            // 分享标题链接
            if (Strings.isNotEmpty(titleUrl)) {
                oks.setUrl(titleUrl);
            }
            // 网络图片地址
            if (Strings.isNotEmpty(imageUrl)) {
                oks.setImageUrl(imageUrl);
            }
            // 本地图片地址
            if (Strings.isNotEmpty(imagePath)) {
                oks.setImagePath(imagePath);
            }
            // 分享文件地址
            if (Strings.isNotEmpty(filePath)) {
                oks.setFilePath(filePath);
            }
            // 分享音乐链接
            if (Strings.isNotEmpty(musicUrl)) {
                oks.setMusicUrl(musicUrl);
            }

            // 指定分享到微信好友
            oks.setPlatform(Wechat.NAME);
            oks.setCallback(getListener());
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }

    private static class WechatMomentsStep extends WechatBaseStep {

        private WechatMomentsStep(Context context, PlatformActionListener listener) {
            super(context, listener);
        }

        @Override
        public void share() {
            OnekeyShare oks = new OnekeyShare();
            // 分享标题
            if (Strings.isNotEmpty(title)) {
                oks.setTitle(title);
            }
            // 分享内容
            if (Strings.isNotEmpty(content)) {
                oks.setText(content);
            }
            // 分享标题链接
            if (Strings.isNotEmpty(titleUrl)) {
                oks.setUrl(titleUrl);
            }
            // 网络图片地址
            if (Strings.isNotEmpty(imageUrl)) {
                oks.setImageUrl(imageUrl);
            }
            // 本地图片地址
            if (Strings.isNotEmpty(imagePath)) {
                oks.setImagePath(imagePath);
            }
            // 分享文件地址
            if (Strings.isNotEmpty(filePath)) {
                oks.setFilePath(filePath);
            }
            // 分享音乐链接
            if (Strings.isNotEmpty(musicUrl)) {
                oks.setMusicUrl(musicUrl);
            }

            // 指定分享到微信好友
            oks.setPlatform(WechatMoments.NAME);
            oks.setCallback(getListener());
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }
}
