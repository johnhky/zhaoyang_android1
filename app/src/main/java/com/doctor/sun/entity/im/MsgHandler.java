package com.doctor.sun.entity.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.Tags;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.media.AudioController;
import com.doctor.sun.ui.activity.FileDetailActivity;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.EditDoctorInfoFragment;
import com.doctor.sun.ui.fragment.EditPatientInfoFragment;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.TimeUtils;
import com.doctor.sun.util.VoipCallUtil;
import com.google.common.base.Strings;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.ganguo.library.util.Files;
import io.ganguo.library.util.Tasks;
import io.realm.Realm;

/**
 * Created by rick on 15/4/2016.
 * 处理im消息
 */
public class MsgHandler {
    public static final String TAG = MsgHandler.class.getSimpleName();
    public static final int WIDTH_PER_SECOND = 25;
    public static final int WIDTH_PER_SECOND_EXCEED_10 = 3;
    private MediaPlayer mSuffixPlayer;

    public void showDoctorAdvice(Context context, final String id) {
        Appointment data = new Appointment() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public double getMoney() {
                return 0;
            }

            @Override
            public String getAddress() {
                return null;
            }

            @Override
            public int getType() {
                return 0;
            }

            @Override
            public String getEnd_time() {
                return null;
            }

            @Override
            public String getBook_time() {
                return null;
            }

            @Override
            public String getRecord_id() {
                return null;
            }

            @Override
            public List<Tags> getSelect_tags() {
                return null;
            }
        };
        AppointmentHandler2.viewDetail(context, 1, data);
    }


    public String showCreatedTime(String time) {
        if (!TextUtils.isEmpty(time)) {
            Date date = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = simpleDateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }

    /*修改个人信息*/
    public void toUpdateData(Context context) {

        if (Settings.isDoctor()) {
            Intent intent = EditDoctorInfoFragment.intentFor(context, Settings.getDoctorProfile());
            context.startActivity(intent);
        } else {
            Intent intent = SingleFragmentActivity.intentFor(context, "我", EditPatientInfoFragment.getArgs(Settings.getPatientProfile()));
            context.startActivity(intent);
        }
    }

    public static void removeMsg(final String msgId) {
        if (msgId == null || "".equals(msgId)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TextMsg target = realm.where(TextMsg.class).equalTo("msgId", msgId).findFirst();
                if (target != null) {
                    target.deleteFromRealm();
                }
            }
        });
    }

    public static void saveMsg(IMMessage msg) {
        saveMsg(msg, false);
    }

    public static void saveMsg(final IMMessage msg, final boolean haveRead) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                Log.e("eeee",msg1.getMsgId()+",from:"+msg1.getFrom()+"content: "+msg1.getBody());
                if (!isValid(msg1)) return;

                if (msg1.getType().equals(MsgTypeEnum.avchat.toString())) {
                    msg1.setHaveRead(true);
                } else {
                    msg1.setHaveRead(haveRead);
                    if (!haveRead && msg.getConfig() != null && msg.getConfig().enablePush) {
                        NotificationUtil.showNotification(msg1);
                    }
                }
                realm.copyToRealmOrUpdate(msg1);
            }
        });
    }

    public static void saveMsgs(final List<IMMessage> msgs) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (IMMessage msg : msgs) {
                    boolean haveRead = NIMConnectionState.passThirtyMinutes(msg);
                    TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                    if (!isValid(msg1)) continue;

                    if (msg1.getType().equals(MsgTypeEnum.avchat.toString())) {
                        msg1.setHaveRead(true);
                    } else {
                        msg1.setHaveRead(haveRead);
                        if (!haveRead && msg.getConfig() != null && msg.getConfig().enablePush) {
                            if (null != msg1) {
                                NotificationUtil.showNotification(msg1);
                            }
                        }
                    }
                    realm.copyToRealmOrUpdate(msg1);

                }
            }
        });
    }

    public static void saveMsgs(final List<IMMessage> msgs, final boolean haveRead) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (IMMessage msg : msgs) {
                    TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                    if (!isValid(msg1)) continue;

                    if (msg1.getType().equals(MsgTypeEnum.avchat.toString())) {
                        msg1.setHaveRead(true);
                    } else {
                        msg1.setHaveRead(haveRead);
                    }
                    realm.copyToRealmOrUpdate(msg1);
                }
            }
        });
    }
    public void revokeMessage(final IMMessage message, final Context context) {
        NIMClient.getService(MsgService.class).revokeMessage(message).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "消息撤回成功!", Toast.LENGTH_SHORT).show();
                removeMsg(message.getUuid());
            }

            @Override
            public void onFailed(int i) {
                if (i == 508) {
                    Toast.makeText(context, "消息已发出两分钟，不能撤回!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("eeee", i + "");
                }
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e("eeee", "throwable: " + throwable.getMessage());
            }
        });
    }

    public boolean showRevokeMessage(final Context context, final TextMsg msg) {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content("确定撤回这条消息吗?")
                .positiveText("撤回")
                .negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                MsgService service = NIMClient.getService(MsgService.class);
                List<String> msgId = new ArrayList<>();
                msgId.add(msg.getMsgId());
                InvocationFuture<List<IMMessage>> invocationFuture = service.queryMessageListByUuid(msgId);
                invocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(final List<IMMessage> imMessages) {
                        if (imMessages.isEmpty()) {
                            return;
                        }
                        revokeMessage(imMessages.get(0), context);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.e("eeee", i + "");
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        Log.e("eeee", throwable.getMessage());
                    }
                });

            }
        });
        builder.show();


        return true;
    }


    public static boolean isValid(TextMsg msg1) {
        return msg1 != null && !Strings.isNullOrEmpty(msg1.getMsgId());
    }

    public void onResendClick(Context context, final String msgId) {
        showResendDialog(context, msgId);
    }

    private void showResendDialog(Context context, final String msgId) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content("重新发送这条消息?")
                .positiveText("重新发送")
                .negativeText("取消");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                resendMessage(msgId);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void resendMessage(final String msgId) {
        Log.d(TAG, "resendMessage() called with: msgId = [" + msgId + "]");
        MsgService service = NIMClient.getService(MsgService.class);
        LinkedList<String> list = new LinkedList<>();
        list.add(msgId);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.queryMessageListByUuid(list);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                Log.d(TAG, "onSuccess() called with: imMessages = [" + imMessages + "]");
                if (imMessages != null && !imMessages.isEmpty()) {
                    for (IMMessage imMessage : imMessages) {
                        if (imMessage.getStatus().equals(MsgStatusEnum.fail)) {
                            Log.e(TAG, "onSuccess: " + imMessage);
                            imMessage.setStatus(MsgStatusEnum.sending);
                            IMManager.getInstance().sendMsg(imMessage, false);
                        }
                    }
                }
            }

            @Override
            public void onFailed(int i) {
                Log.e(TAG, "onFailed: " + i);
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, "onException() called with: throwable = [" + throwable + "]");
            }
        });


    }

    /* 短语音消息  消息长度,点击事件,
     *
     */
    public int msgWidth(long duration) {
        if (duration < 10) {
            return (int) (WIDTH_PER_SECOND * duration + 50);
        } else {
            return (int) (300 + WIDTH_PER_SECOND_EXCEED_10 * (duration - 10));
        }
    }

    public View.OnClickListener onAudioClick(final TextMsg data) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                FrameLayout frameLayout = (FrameLayout) v;
                final ImageView imageView = (ImageView) frameLayout.getChildAt(0);
                play(imageView);
                VoipCallUtil.enableScreenSensor(v.getContext());

                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String url = data.attachmentData("url");
                        AudioController.getInstance().play(url, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                stop(imageView);
                                playSuffix(v.getContext());

                                Realm defaultInstance = Realm.getDefaultInstance();
                                defaultInstance.executeTransaction(
                                        setHaveListenTransaction(data));
                                VoipCallUtil.disableScreenSensor(v.getContext());
                            }
                        });
                    }
                }, 500);
            }
        };
    }

    @NonNull
    private Realm.Transaction setHaveListenTransaction(final TextMsg data) {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                data.setHaveListen(true);
            }
        };
    }

    private void play(View animationView) {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.start();
        }
    }

    private void stop(View animationView) {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.stop();
            animation.selectDrawable(0);
        }
    }

    protected void playSuffix(Context mContext) {
        mSuffixPlayer = MediaPlayer.create(mContext, R.raw.audio_end_tip);
        mSuffixPlayer.setLooping(false);
        mSuffixPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
        mSuffixPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mSuffixPlayer.release();
                mSuffixPlayer = null;
            }
        });
        mSuffixPlayer.start();
    }

    /*  文件消息 点击事件,显示图片等
     *
     */
    public int drawableForFileType(String extension) {
        return FileTypeMap.getDrawable(extension);
    }

    public View.OnClickListener fileDetail(final TextMsg msg) {
        final String extension = msg.attachmentData("extension");
        final String url = msg.attachmentData("url");
        final long fileSize = Long.parseLong(msg.attachmentData("fileSize"));
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = FileDetailActivity.makeIntent(v.getContext(), extension, url, fileSize);
                v.getContext().startActivity(i);
            }
        };
    }

    public String fileSize(long size) {
        return size / 1024 + "KB";
    }

    public String getRelativeTime(long time) {
        return TimeUtils.formatChatMsgShortDate(time);
    }

    public String statusText(String status) {
        if (String.valueOf(MsgStatusEnum.fail).equals(status)) {
            return "发送失败";
        } else {
            return "正在发送";
        }
    }

    public boolean localPathExits(TextMsg msg) {
        String path = msg.attachmentData("path");
        if (null == path || "".equals(path)) {
            return false;
        }
        return Files.checkFileExists(path.replace("file://", ""));
    }
}
