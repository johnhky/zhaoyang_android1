package io.ganguo.opensdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.ganguo.library.util.Strings;

/**
 * Step Builder
 * 新浪微博
 * Created by Lynn on 2/19/16.
 */
public final class ShareSinaWeiboStep extends BaseStepBuilder {
    private ShareSinaWeiboStep() {
    }

    public static ContentStep SinaWeibo(Context context, PlatformActionListener listener) {
        return new SinaWeibo(context, listener);
    }

    public interface ContentStep {
        OptionsStep setContent(@NonNull String content);
    }

    public interface OptionsStep extends OptionalStep {
        OptionsStep setLatitude(float latitude);

        OptionsStep setLongitude(float longitude);

        OptionsStep setImageUrl(@Nullable String imageUrl);

        OptionsStep setImagePath(@Nullable String imagePath);
    }

    private static class SinaWeibo extends BaseStep implements ContentStep, OptionsStep {
        private SinaWeibo(Context context, PlatformActionListener listener) {
            super(context, listener);
        }

        /**
         * 分享内容 必填参数 不能超过140个汉字
         */
        private String content;

        /**
         * 分享网络图片地址 可选填参数
         * 图片最大5M 仅支持JPEG GIF、PNG格式
         * <p/>
         * 注意：需要申请高级写入接口，否则会分享失败
         */
        private String imageUrl;

        /**
         * 分享本地图片路径 可选填参数
         */
        private String imagePath;

        /**
         * 纬度 -90.0到+90.0 +表示北纬 可选填参数
         */
        private float latitude;

        /**
         * 经度 -180.0到+180.0 +表示东经 可选填参数
         */
        private float longitude;

        @Override
        public OptionsStep setContent(@NonNull String content) {
            this.content = content;
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
            oks.setPlatform(cn.sharesdk.sina.weibo.SinaWeibo.NAME);
            oks.setCallback(getListener());
            // 显示编辑页
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(getContext());
        }
    }
}
