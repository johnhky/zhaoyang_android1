package io.ganguo.opensdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qzone.QZone;
import io.ganguo.library.util.Strings;

/**
 * Step Builder
 * QQZone Share
 * Created by Lynn on 2/19/16.
 */
public final class ShareQQZoneStepBuilder extends BaseStepBuilder {
    private ShareQQZoneStepBuilder() {
    }

    public static TitleStep QQZone(Context context, PlatformActionListener listener) {
        return new QQZoneStep(context, listener);
    }

    public interface TitleStep {
        ContentStep setTitle(@NonNull String name);
    }

    public interface ContentStep {
        SiteNameStep setContent(@NonNull String content);
    }

    public interface SiteNameStep {
        SiteUrlStep setSiteName(@NonNull String siteName);
    }

    public interface SiteUrlStep {
        OptionsStep setSiteUrl(@NonNull String siteUrl);
    }

    public interface OptionsStep extends OptionalStep {
        OptionsStep setTitleUrl(@NonNull String url);

        OptionsStep setImageUrl(@Nullable String imageUrl);

        OptionsStep setImagePath(@Nullable String imagePath);
    }

    private static class QQZoneStep extends BaseStep implements TitleStep, ContentStep, SiteNameStep,
            SiteUrlStep, OptionsStep {
        private QQZoneStep(Context context, PlatformActionListener listener) {
            super(context, listener);
        }

        /**
         * 分享标题 必填参数 最多200个字符
         */
        private String title;

        /**
         * 分享标题链接 必填参数
         */
        private String titleUrl;

        /**
         * 分享内容 必填参数 最多600个字符
         */
        private String content;

        /**
         * 网站名称 必填参数
         */
        private String siteName;

        /**
         * 网站链接 必填参数
         */
        private String siteUrl;

        /**
         * 分享网络图片地址 可选填参数
         */
        private String imageUrl;

        /**
         * 分享本地图片路径 可选填参数
         */
        private String imagePath;

        @Override
        public SiteNameStep setContent(@NonNull String content) {
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
        public ShareStep commit() {
            return this;
        }

        @Override
        public SiteUrlStep setSiteName(@NonNull String siteName) {
            this.siteName = siteName;
            return this;
        }

        @Override
        public OptionsStep setSiteUrl(@NonNull String siteUrl) {
            this.siteUrl = siteUrl;
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

        @Override
        public void share() {
            OnekeyShare oks = new OnekeyShare();
            // 分享标题
            if (Strings.isNotEmpty(title)) {
                if (title.length() > 30) {
                    oks.setTitle(title.substring(0, 25) + "…");
                } else {
                    oks.setTitle(title);
                }
            }
            // 分享标题链接
            if (Strings.isNotEmpty(titleUrl)) {
                oks.setTitleUrl(titleUrl);
            }
            // 分享内容
            if (Strings.isNotEmpty(content)) {
                if (content.length() > 40) {
                    oks.setText(content.substring(0, 34) + "…");
                } else {
                    oks.setText(content);
                }
            }
            // 网络图片地址
            if (Strings.isNotEmpty(imageUrl)) {
                oks.setImageUrl(imageUrl);
            }
            // 本地图片路径
            if (Strings.isNotEmpty(imagePath)) {
                oks.setImagePath(imagePath);
            }
            // 网站名称
            if (Strings.isNotEmpty(siteName)) {
                oks.setSite(siteName);
            }
            // 网站地址
            if (Strings.isNotEmpty(siteUrl)) {
                oks.setSiteUrl(siteUrl);
            }

            // 分享到QZone
            oks.setPlatform(QZone.NAME);
            oks.setCallback(getListener());
            // 显示编辑页
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }
}
