package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.HelpActivity;
import com.doctor.sun.ui.activity.LoginActivity;
import com.doctor.sun.ui.activity.doctor.AdviceActivity;
import com.doctor.sun.ui.activity.doctor.PasswordActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.ShareDialog;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.opensdk.*;

/**
 * Created by lucas on 12/22/15.
 */
public class SettingHandler extends BaseHandler {

    public SettingHandler(Activity context) {
        super(context);
    }


    public void changePassword(View view) {
        Intent intent = PasswordActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void giveAdvice(View view) {
        Intent intent = AdviceActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void help(View view) {
        Intent intent = HelpActivity.makeIntent(view.getContext(), Config.getInt(Constants.USER_TYPE, -1));
        view.getContext().startActivity(intent);
    }

    private PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.d("TAG", "onComplete: " + "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.d("TAG", "onError: " + "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.d("TAG", "onCancel: " + "分享被取消");
        }
    };

    public OnItemClickListener share() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, final View view, BaseViewHolder vh) {
                ShareDialog.showShareDialog(view.getContext(), new ShareDialog.GetActionButton() {
                    @Override
                    public void onClickMicroblogButton() {
                        ShareManager shareManager = new ShareManager(view.getContext(), platformActionListener);
                        shareManager.shareSinaWeibo()
                                .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App。")
                                .commit()
                                .share();
                    }

                    @Override
                    public void onClickFriendButton() {
                        ShareManager shareManager = new ShareManager(view.getContext(), platformActionListener);
                        shareManager.shareWechatMoments()
                                .setTitle("【昭阳医生】一个专业的心理/精神科咨询平台")
                                .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App。")
                                .setImageUrl("http://ganguo.io/images/android.png")
                                .commit()
                                .share();
                    }

                    @Override
                    public void onClickWeChatButton() {
                        ShareManager shareManager = new ShareManager(view.getContext(), platformActionListener);
                        shareManager.shareWechat()
                                .setTitle("【昭阳医生】一个专业的心理/精神科咨询平台")
                                .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App。")
                                .commit()
                                .share();
                    }

                    @Override
                    public void onClickQqButton() {
                        ShareManager shareManager = new ShareManager(view.getContext(), platformActionListener);
                        shareManager.shareQQ()
                                .setTitle("【昭阳医生】一个专业的心理/精神科咨询平台")
                                .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App。")
                                .commit()
                                .share();
                    }
                });
            }
        };
    }

    public void checkUpdate(View view) {
        ToastHelper.showMessage(view.getContext(), "已经是最新版");
    }

    public void logOut(final View view) {
        AuthModule api = Api.of(AuthModule.class);
        api.logout(Config.getString(Constants.TOKEN)).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Config.putString(Constants.TOKEN, null);
                Config.putInt(Constants.USER_TYPE, -1);
                Config.putString(Constants.VOIP_ACCOUNT, "");
                Intent intent = LoginActivity.makeIntent(view.getContext());
                Messenger.getInstance().logout();
                view.getContext().startActivity(intent);
                AppManager.finishAllActivity();
            }
        });
    }

    public interface GetWindowSize {
        int getWindowSize();
    }
}
