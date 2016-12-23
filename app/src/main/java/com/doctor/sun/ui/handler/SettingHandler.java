package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.LoginActivity;
import com.doctor.sun.ui.activity.PMainActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.AdviceActivity;
import com.doctor.sun.ui.activity.doctor.MainActivity;
import com.doctor.sun.ui.activity.doctor.MeActivity;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.activity.patient.PMeActivity;
import com.doctor.sun.ui.fragment.AllowToSearchFragment;
import com.doctor.sun.ui.fragment.ChangePasswordFragment;
import com.doctor.sun.ui.widget.ShareDialog;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.opensdk.ShareManager;
import io.realm.Realm;

/**
 * Created by lucas on 12/22/15.
 */
public class SettingHandler {
    public static final String imagePath = Config.getImagePath() + "/ic_share.png";


    public void allowToSearch(Context context) {
        AllowToSearchFragment.startFrom(context);
    }

    public void changePassword(View view) {
        Bundle bundle = ChangePasswordFragment.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(view.getContext(), "修改密码", bundle);
        view.getContext().startActivity(intent);
    }

    public void giveAdvice(View view) {
        Intent intent = AdviceActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void help(View view) {
        ShowCaseUtil.reset();
        Context context = view.getContext();
        if (Settings.isDoctor()) {

            Intent meActivity = MeActivity.makeIntent(context);
            startShowCase(context, meActivity);

            Intent intent = MainActivity.makeIntent(context);
            startShowCase(context, intent);
        } else {
            Intent pMe = PMeActivity.makeIntent(context);
            startShowCase(context, pMe);

            Intent consulting = PConsultingActivity.makeIntent(context);
            startShowCase(context, consulting);

            Intent intent = PMainActivity2.makeIntent(context);
            startShowCase(context, intent);
        }
    }

    private void startShowCase(Context context, Intent meActivity) {
        meActivity.putExtra(Constants.IS_SHOWCASE, true);
        context.startActivity(meActivity);
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

    public void share(final Context context) {
        ShareDialog.showShareDialog(context, new ShareDialog.GetActionButton() {
            @Override
            public void onClickMicroblogButton() {
                ShareManager shareManager = new ShareManager(context, platformActionListener);
                shareManager.shareSinaWeibo()
                        .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App http://wechat.zhaoyang120.cn/auth/download.html")
                        .commit()
                        .share();
            }

            @Override
            public void onClickFriendButton() {
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = "http://wechat.zhaoyang120.cn/auth/download.html";
                WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
                mediaMessage.title = "【昭阳医生】一个专业的心理/精神科咨询平台";
                mediaMessage.description = "这是一个能让咨询者与心理/精神科医生轻松交流的App。";
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mediaMessage.thumbData = stream.toByteArray();

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "webpage";
                req.message = mediaMessage;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;

                IWXAPI wxapi = WXAPIFactory.createWXAPI(context, "wxe541efd34c189cf1");
                wxapi.sendReq(req);
            }

            @Override
            public void onClickWeChatButton() {
                WXWebpageObject webpageObject = new WXWebpageObject();
                webpageObject.webpageUrl = "http://wechat.zhaoyang120.cn/auth/download.html";
                WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
                mediaMessage.title = "【昭阳医生】一个专业的心理/精神科咨询平台";
                mediaMessage.description = "这是一个能让咨询者与心理/精神科医生轻松交流的App。";
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mediaMessage.thumbData = stream.toByteArray();

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = "webpage";
                req.message = mediaMessage;
                req.scene = SendMessageToWX.Req.WXSceneSession;

                IWXAPI wxapi = WXAPIFactory.createWXAPI(context, "wxe541efd34c189cf1");
                wxapi.sendReq(req);

            }

            @Override
            public void onClickQqButton() {
                ShareManager shareManager = new ShareManager(context, platformActionListener);
                shareManager.shareQQ()
                        .setTitle("【昭阳医生】一个专业的心理/精神科咨询平台")
                        .setContent("这是一个能让咨询者与心理/精神科医生轻松交流的App。")
                        .setTitleUrl("http://wechat.zhaoyang120.cn/auth/download.html")
                        .setImageUrl("http://www.zhaoyang120.com/common/image/logo-nav.png")
                        .commit()
                        .share();
            }
        });
    }

    public View.OnClickListener checkUpdate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity context = (Activity) v.getContext();
                UpdateUtil.reset();
                UpdateUtil.setNoNewVersion(new UpdateUtil.onNoNewVersion() {
                    @Override
                    public void onNoNewVersion() {
                        Toast.makeText(context, "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
                    }
                });
                UpdateUtil.checkUpdate(context);
            }
        };
    }

    public void logOut(final View view) {
        AuthModule api = Api.of(AuthModule.class);
        api.logout(Config.getString(Constants.TOKEN)).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                clearUserData();
                Intent intent = LoginActivity.makeIntent(view.getContext());
                IMManager.getInstance().logout();
                view.getContext().startActivity(intent);
                AppManager.finishAllActivity();
            }
        });
    }

    public String appVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public interface GetWindowSize {
        int getWindowSize();
    }

    private static class ClearAllTransaction implements Realm.Transaction {
        @Override
        public void execute(Realm realm) {
            realm.where(TextMsg.class).findAll().deleteAllFromRealm();
        }
    }

    public static void clearUserData() {
        Config.putString(Constants.TOKEN, null);
        Config.putInt(Constants.USER_TYPE, -1);
        Config.putString(Constants.VOIP_ACCOUNT, "");
        Settings.setPatientProfile(null);
        Config.putString(Constants.DOCTOR_PROFILE, null);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new ClearAllTransaction());
    }
}
