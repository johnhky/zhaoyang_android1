package com.doctor.sun.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.doctor.sun.databinding.DialogShareBinding;
import com.doctor.sun.ui.handler.SettingHandler;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * Created by lucas on 12/22/15.
 */
public class ShareDialog extends BaseDialog{

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    public interface GetActionButton {
        void onClickMicroblogButton();
        void onClickFriendButton();
        void onClickWeChatButton();
        void onClickQqButton();
    }

    public static void showShareDialog(Context context, final GetActionButton getActionButton) {
        final ShareDialog shareDialog = new ShareDialog(context);
        DialogShareBinding binding = DialogShareBinding.inflate(LayoutInflater.from(context));
        shareDialog.setContentView(binding.getRoot());
        shareDialog.show();

        WindowManager.LayoutParams lp = shareDialog.getWindow().getAttributes();
        SettingHandler.GetWindowSize getWindowSize = (SettingHandler.GetWindowSize) context;
        int windowSize = getWindowSize.getWindowSize();
        lp.width = windowSize;
        shareDialog.getWindow().setAttributes(lp);

        binding.ivMicroblog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickMicroblogButton();
            }
        });

        binding.ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickFriendButton();
            }
        });

        binding.ivWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickWeChatButton();
            }
        });

        binding.ivQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActionButton.onClickQqButton();
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.dismiss();
            }
        });
    }
}
