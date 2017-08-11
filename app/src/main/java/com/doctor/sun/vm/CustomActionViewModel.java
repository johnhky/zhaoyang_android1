package com.doctor.sun.vm;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.StringBoolean;
import com.doctor.sun.event.CallServiceEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.NimMsgInfo;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.util.PermissionUtil;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import java.io.File;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 13/4/2016.
 */
public class CustomActionViewModel {
    public static final int IMAGE_REQUEST_CODE = 100;
    public static final int VIDEO_REQUEST_CODE = 101;
    public static final int CALL_PHONE_ALLOW = 111;
    public static final int CALL_PHONE_NOT_ALLOW = 112;
    private Activity mActivity;

    public CustomActionViewModel(Context context) {
        this.mActivity = (Activity) context;
    }

    @NonNull
    public SimpleAdapter getSimpleAdapter(int fromMedicineStore) {
        SimpleAdapter adapter = new SimpleAdapter();
        if (fromMedicineStore == IntBoolean.FALSE) {
            adapter.add(audioChat("语音电话"));
        } else if (fromMedicineStore == IntBoolean.NOT_GIVEN) {
            adapter.add(audioChatMenu("客服电话",CALL_PHONE_ALLOW));
        }else if(fromMedicineStore==IntBoolean.TRUE){
            adapter.add(audioChatMenu("语音电话",CALL_PHONE_NOT_ALLOW));
        }
        adapter.add(galleryMenu());
        adapter.add(cameraMenu());
        if (fromMedicineStore == IntBoolean.FALSE) {
            adapter.add(videoChatMenu());
        }
        adapter.add(chooseFileMenu());
//        ClickMenu object = extendTimeMenu();
//        object.setEnable(true);
//        adapter.add(object);

        adapter.onFinishLoadMore(true);
        return adapter;
    }

    @NonNull
    private ClickMenu chooseFileMenu() {
        return new ClickMenu(R.layout.item_chat_menu, R.drawable.message_plus_file_selector2, "文件传输", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser.showFileChooser((Activity) v.getContext());
            }
        });
    }


    private void askConfirmation(Context context, String question) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
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
        return new ClickMenu(R.layout.item_chat_menu, R.drawable.nim_message_plus_video_selector2, "拍摄", new View.OnClickListener() {
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
                                        try {
                                            final Uri image = getFileUrlForCameraRequest(mActivity);
                                            Intent intentFromCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                ClipData clip = ClipData.newRawUri(null, image);
                                                intentFromCamera.setClipData(clip);
                                            }
                                            intentFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, image);
                                            intentFromCamera.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                            intentFromCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            mActivity.startActivityForResult(intentFromCamera, VIDEO_REQUEST_CODE);
                                        } catch (Exception e) {
                                            Toast.makeText(mActivity, "无法打开拍摄应用", Toast.LENGTH_SHORT).show();
                                        }
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

    public static Uri getFileUrlForCameraRequest(Context mActivity) {
        return FileProvider.getUriForFile(mActivity, BuildConfig.FILE_PROVIDER, getVideoTempFile());
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
        return new File(Config.getTempPath(), "videoFromCamera");
    }

    @NonNull
    private ClickMenu galleryMenu() {
        return new ClickMenu(R.layout.item_chat_menu, R.drawable.nim_message_plus_photo_selector2, "相册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.openGallery(mActivity, IMAGE_REQUEST_CODE);
            }
        });
    }
@NonNull
private ClickMenu audioChat(String title){
 return    new ClickMenu(R.layout.item_chat_menu, R.drawable.nim_message_plus_phone2, title, new View.OnClickListener() {
     @Override
     public void onClick(View v) {
            if(Settings.isDoctor()){
                NimMsgInfo nimTeamId = (NimMsgInfo) mActivity;
                startAVChat(nimTeamId, AVChatType.AUDIO.getValue(), nimTeamId.getDuration());
            }else{
                tryStartAVChat(v, AVChatType.AUDIO.getValue());
            }
     }
 });
}
    @NonNull
    private ClickMenu audioChatMenu(String title, final int type) {
        return new ClickMenu(R.layout.item_chat_menu, R.drawable.nim_message_plus_phone2, title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.isDoctor()){
                    TwoChoiceDialog.show(mActivity, "4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
                        @Override
                        public void onApplyClick(MaterialDialog dialog) {
                            try {
                                Uri uri = Uri.parse("tel:4008352600");
                                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                mActivity.startActivity(intent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelClick(MaterialDialog dialog) {

                        }
                    });
                }else{
                    switch (type){
                        case CALL_PHONE_ALLOW:
                            TwoChoiceDialog.show(mActivity, "4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
                                @Override
                                public void onApplyClick(MaterialDialog dialog) {
                                    try {
                                        Uri uri = Uri.parse("tel:4008352600");
                                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                        mActivity.startActivity(intent);
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelClick(MaterialDialog dialog) {

                                }
                            });
                            break;
                        case CALL_PHONE_NOT_ALLOW:
                            Toast.makeText(mActivity,"寄药小助手暂时不支持语音电话！",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                }

          /*      if (Settings.isDoctor()) {
                   *//* NimMsgInfo nimTeamId = (NimMsgInfo) mActivity;
                    startAVChat(nimTeamId, AVChatType.AUDIO.getValue(), nimTeamId.getDuration());*//*
                    TwoChoiceDialog.show(mActivity, "020-4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
                        @Override
                        public void onApplyClick(MaterialDialog dialog) {
                            try {
                                Uri uri = Uri.parse("tel:4008352600");
                                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                                mActivity.startActivity(intent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelClick(MaterialDialog dialog) {

                        }
                    });
                } else {
                   *//* tryStartAVChat(v, AVChatType.AUDIO.getValue());*//*

                }*/
            }
        });
    }

    @NonNull
    private ClickMenu videoChatMenu() {
        return new ClickMenu(R.layout.item_chat_menu, R.drawable.message_plus_video_chat_selector2, "视频聊天", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.isDoctor()) {
                    NimMsgInfo nimTeamId = (NimMsgInfo) mActivity;
                    startAVChat(nimTeamId, AVChatType.VIDEO.getValue(), nimTeamId.getDuration());
                } else {
                    tryStartAVChat(v, AVChatType.VIDEO.getValue());
                }
            }
        });
    }

    private void tryStartAVChat(final View view, final int AVChatType) {
        NimMsgInfo nimTeamId = (NimMsgInfo) mActivity;
        if (nimTeamId.shouldAskServer()) {
            AppointmentModule api = Api.of(AppointmentModule.class);
            api.canUse(AVChatType, nimTeamId.appointmentId()).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    if (StringBoolean.TRUE.equals(response)) {
                        NimMsgInfo nimTeamId = (NimMsgInfo) mActivity;
                        startAVChat(nimTeamId, AVChatType, nimTeamId.getDuration());
                    } else {
                        askConfirmation(view.getContext(), "医生因个人原因暂时停止该功能，请用文字、图片等继续与医生咨询");
                    }
                }
            });
        } else {
            if (mActivity instanceof MedicineStoreActivity) {
                EventHub.post(new CallServiceEvent());
            } else {
                askConfirmation(view.getContext(), "该功能仅限于VIP网诊的就诊时间内使用");
            }
        }
    }

    private void startAVChat(NimMsgInfo nimTeamId, int AVChatType, int duration) {
        AVChatActivity.start(mActivity, nimTeamId.getTargetP2PId(), AVChatType, AVChatActivity.FROM_INTERNAL, duration);
    }
}
