package com.doctor.sun.entity.im;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.doctor.sun.im.custom.FileTypeMap;
import com.doctor.sun.media.AudioManager;
import com.doctor.sun.ui.activity.FileDetailActivity;

import io.realm.Realm;

/**
 * Created by rick on 15/4/2016.
 */
public class MsgHandler {
    public static final String TAG = MsgHandler.class.getSimpleName();
    public static final int WIDTH_PER_SECOND = 25;
    public static final int WIDTH_PER_SECOND_EXCEED_10 = 3;

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
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) v;
                final ImageView imageView = (ImageView) frameLayout.getChildAt(0);
                play(imageView);
                AudioManager.getInstance().play(data.getMessageStatus(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stop(imageView);

                        Realm defaultInstance = Realm.getDefaultInstance();
                        defaultInstance.executeTransaction(
                                setHaveReadTransaction(data));
                    }
                });
            }
        };
    }

    @NonNull
    private Realm.Transaction setHaveReadTransaction(final TextMsg data) {
        return new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                data.setHaveRead(true);
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

    /*  文件消息 点击事件,显示图片等
     *
     */
    public int drawableForFileType(String extension) {
        return FileTypeMap.getDrawable(extension);
    }

    public View.OnClickListener fileDetail(final TextMsg msg) {
        final String extension = msg.getUserData();
        final String url = msg.getMessageStatus();
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = FileDetailActivity.makeIntent(v.getContext(), extension, url, msg.getDuration());
                v.getContext().startActivity(i);
            }
        };
    }

    public String fileSize(long size) {
        return size / 1024 + "KB";
    }
}
