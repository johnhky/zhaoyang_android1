package io.ganguo.opensdk.login;

import android.view.View;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.R;
import io.ganguo.library.ui.activity.BaseActivity;

/**
 * 第三方登录
 * <p/>
 * Created by Tony on 10/24/15.
 */
public class LoginDemoActivity extends BaseActivity implements View.OnClickListener {

    private View action_login_qq;
    private View action_login_wechat;
    private View action_login_sina_weibo;

    /**
     * 第三方登录监听器
     * 不要在activity中implements，会关联当前activity无法释放
     */
    private PlatformActionListener mLoginListener = new PlatformActionListener() {
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
        setContentView(R.layout.activity_login_demo);
    }

    @Override
    public void initView() {
        action_login_qq = findViewById(R.id.action_login_qq);
        action_login_wechat = findViewById(R.id.action_login_wechat);
        action_login_sina_weibo = findViewById(R.id.action_login_sina_weibo);
    }

    @Override
    public void initListener() {
        action_login_qq.setOnClickListener(this);
        action_login_wechat.setOnClickListener(this);
        action_login_sina_weibo.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == action_login_qq) {
            loginQQ();
        } else if (v == action_login_wechat) {
            loginWechat();
        } else if (v == action_login_sina_weibo) {
            loginSinaWeibo();
        }
    }

    /**
     * 通过QQ登录
     */
    private void loginQQ() {
        OpenLogin.loginQQ(this, mLoginListener);
    }

    /**
     * 通过微信登录
     */
    private void loginWechat() {
        OpenLogin.loginWechat(this, mLoginListener);
    }

    /**
     * 通过微博登录
     */
    private void loginSinaWeibo() {
        OpenLogin.loginSinaWeibo(this, mLoginListener);
    }

}
