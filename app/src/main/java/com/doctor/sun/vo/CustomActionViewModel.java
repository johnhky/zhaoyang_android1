package com.doctor.sun.vo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.util.PermissionUtil;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import java.io.File;

import io.ganguo.library.Config;

/**
 * Created by rick on 13/4/2016.
 */
public class CustomActionViewModel {
    public static final int IMAGE_REQUEST_CODE = 100;
    public static final int VIDEO_REQUEST_CODE = 101;

    private Activity mActivity;
    private AudioChatCallback data;

    public CustomActionViewModel(Context context, AudioChatCallback data) {
        this.mActivity = (Activity) context;
        this.data = data;
    }

    @NonNull
    public SimpleAdapter getSimpleAdapter() {
        SimpleAdapter adapter = new SimpleAdapter(mActivity);

        adapter.add(audioChatMenu());
        adapter.add(galleryMenu());
        adapter.add(cameraMenu());
        adapter.add(videoChatMenu());
        adapter.add(chooseFileMenu());

        adapter.onFinishLoadMore(true);
        return adapter;
    }

    @NonNull
    private ClickMenu chooseFileMenu() {
        return new ClickMenu(R.layout.item_menu2, R.drawable.message_plus_file_selector, "文件传输", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser.showFileChooser((Activity) v.getContext());
            }
        });
    }

    @NonNull
    private ClickMenu videoChatMenu() {
        return new ClickMenu(R.layout.item_menu2, R.drawable.message_plus_video_chat_selector, "视频聊天", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.isDoctor()) {
                    NimTeamId nimTeamId = (NimTeamId) mActivity;
                    AVChatActivity.start(mActivity, nimTeamId.getP2PId(), AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL);
                } else {
                    alertNotAvailable(v);
                }
            }
        });
    }

    private void alertNotAvailable(View v) {
        String question = "该功能暂未开通，敬请期待";
        MaterialDialog.Builder builder = new MaterialDialog.Builder(v.getContext())
                .content(question)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @NonNull
    private ClickMenu cameraMenu() {
        return new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_video_selector, "拍摄", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext()).items("拍摄照片", "拍摄视频").itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0: {
                                PickImageDialog.openCamera(mActivity, IMAGE_REQUEST_CODE);
                                break;
                            }
                            case 1: {
                                checkPermission(mActivity, new Runnable() {
                                    @Override
                                    public void run() {
                                        final Uri image = Uri.fromFile(getVideoTempFile());
                                        Intent intentFromCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                                        intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, image);
                                        mActivity.startActivityForResult(intentFromCamera, VIDEO_REQUEST_CODE);
                                    }
                                });
                                break;
                            }
                        }
                    }
                }).show();
            }
        });
    }

    public static void checkPermission(Activity mActivity, Runnable runnable) {
        boolean hasPermission = PermissionUtil.hasSelfPermission(mActivity,
                PickImageDialog.PERMISSIONS);
        if (hasPermission) {
            runnable.run();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.requestPermissions(PickImageDialog.PERMISSIONS, VIDEO_REQUEST_CODE);
            }
        }
    }

    @NonNull
    public static File getVideoTempFile() {
        return new File(Config.getDataPath(), "video");
    }

    @NonNull
    private ClickMenu galleryMenu() {
        return new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_photo_selector, "相册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.openGallery(mActivity, IMAGE_REQUEST_CODE);
            }
        });
    }

    @NonNull
    private ClickMenu audioChatMenu() {
        return new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_phone, "语音电话", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.startAudioChat(v);
            }
        });
    }

    public interface AudioChatCallback {
        void startAudioChat(View v);
    }

}
