package com.doctor.sun.entity.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.doctor.sun.R;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.media.AudioController;
import com.doctor.sun.ui.activity.FileDetailActivity;
import com.doctor.sun.util.NotificationUtil;
import com.doctor.sun.util.TimeUtils;
import com.doctor.sun.util.VoipCallUtil;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

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

    public static void saveMsg(IMMessage msg) {
        saveMsg(msg, false);
    }

    public static void saveMsg(final IMMessage msg, final boolean haveRead) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TextMsg msg1 = TextMsgFactory.fromYXMessage(msg);
                if (msg1 == null) return;

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
                    if (msg1 == null) continue;

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
                    if (msg1 == null) continue;

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


}
