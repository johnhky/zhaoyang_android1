package io.ganguo.opensdk.share;

import android.view.View;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.R;
import io.ganguo.library.ui.activity.BaseActivity;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import io.ganguo.opensdk.ShareQQStepBuilder;
import io.ganguo.opensdk.bean.QQShare;
import io.ganguo.opensdk.bean.SinaWeiboShare;
import io.ganguo.opensdk.bean.WechatShare;

/**
 * 分享测试用例
 * <p/>
 * Created by Tony on 10/23/15.
 */
public class ShareDemoActivity extends BaseActivity implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(ShareDemoActivity.class);

    private View action_share_qq;
    private View action_share_wechat;
    private View action_share_moments;
    private View action_share_sina;

    /**
     * 分享监听器
     * 不要在activity中implements，会关联当前activity无法释放
     */
    private PlatformActionListener mShareListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            logger.i("onComplete " + platform.getName());
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            logger.i("onError " + platform.getName());
        }

        @Override
        public void onCancel(Platform platform, int i) {
            logger.i("onCancel " + platform.getName());
        }
    };

    @Override
    public void beforeInitView() {
        setContentView(R.layout.activity_share_demo);
    }

    @Override
    public void initView() {
        action_share_qq = findViewById(R.id.action_share_qq);
        action_share_wechat = findViewById(R.id.action_share_wechat);
        action_share_moments = findViewById(R.id.action_share_moments);
        action_share_sina = findViewById(R.id.action_share_sina);
    }

    @Override
    public void initListener() {
        action_share_qq.setOnClickListener(this);
        action_share_wechat.setOnClickListener(this);
        action_share_moments.setOnClickListener(this);
        action_share_sina.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        if (v == action_share_qq) {
            actionShareQQ();
        } else if (v == action_share_wechat) {
            actionShareWechat();
        } else if (v == action_share_moments) {
            actionShareMoments();
        } else if (v == action_share_sina) {
            actionShareSina();
        }
    }

    private void actionShareQQ() {
        QQShare shareInfo = new QQShare();
        shareInfo.setTitle("甘果移动");
        shareInfo.setTitleUrl("http://www.ganguo.io");
        shareInfo.setContent("珠三角靠谱的移动开发团队");
        shareInfo.setImageUrl("http://ganguo.io/images/android.png");
        ShareHelper.shareQQ(this, shareInfo, mShareListener);

        logger.d(shareInfo);
    }

    private void actionShareWechat() {
        WechatShare shareInfo = new WechatShare();
        shareInfo.setTitle("甘果移动");
        shareInfo.setTitleUrl("http://ganguo.io");
        shareInfo.setContent("珠三角靠谱的移动开发团队");
        shareInfo.setImageUrl("http://ganguo.io/images/android.png");
        ShareHelper.shareWechat(this, shareInfo, mShareListener);

        logger.d(shareInfo);
    }

    private void actionShareMoments() {
        WechatShare shareInfo = new WechatShare();
        shareInfo.setTitle("甘果移动");
        shareInfo.setTitleUrl("http://www.ganguo.io");
        shareInfo.setContent("珠三角靠谱的移动开发团队");
        shareInfo.setImageUrl("http://ganguo.io/images/android.png");
        ShareHelper.shareWechatMoments(this, shareInfo, mShareListener);

        logger.d(shareInfo);
    }

    private void actionShareSina() {
        SinaWeiboShare shareInfo = new SinaWeiboShare();
        shareInfo.setContent("甘果移动 珠三角靠谱的移动开发团队 http://www.ganguo.io");
        shareInfo.setImageUrl("http://ganguo.io/images/android.png");
        ShareHelper.shareSinaWeibo(this, shareInfo, mShareListener);

        logger.d(shareInfo);
    }

}
