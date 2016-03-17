package io.ganguo.opensdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import io.ganguo.library.util.Strings;

/**
 * Step Builder
 * QQ Share
 * Created by Lynn on 2/18/16.
 */
public final class ShareQQStepBuilder extends BaseStepBuilder {
    private ShareQQStepBuilder() {
    }

    public static TitleStep QQ(Context context, PlatformActionListener listener) {
        return new QQStep(context, listener);
    }

    public interface TitleStep {
        ContentStep setTitle(@NonNull String name);
    }

    public interface ContentStep {
        OptionsStep setContent(@NonNull String content);
    }

    public interface OptionsStep extends OptionalStep {
        OptionsStep setTitleUrl(@NonNull String url);

        OptionsStep setImageUrl(@Nullable String imageUrl);

        OptionsStep setImagePath(@Nullable String imagePath);
    }

    private static class QQStep extends BaseStep implements TitleStep, ContentStep,
            OptionsStep {

        /**
         * 分享标题 必填参数 最多30个字符
         */
        private String title;

        /**
         * 标题链接 必填参数
         */
        private String titleUrl;

        /**
         * 分享内容 必填参数 最多40个字符
         */
        private String content;

        /**
         * 分享网络图片地址 可选填参数
         */
        private String imageUrl;

        /**
         * 分享本地图片路径 可选填参数
         */
        private String imagePath;

        private QQStep(Context context, PlatformActionListener listener) {
            super(context, listener);
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
        public OptionsStep setImagePath(@Nullable String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        @Override
        public ShareStep commit() {
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
            // 图片的本地路径
            if (Strings.isNotEmpty(imagePath)) {
                oks.setImagePath(imagePath);
            }

            // 分享到QQ
            oks.setPlatform(QQ.NAME);
            oks.setCallback(getListener());
            // 显示编辑页
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }
}
