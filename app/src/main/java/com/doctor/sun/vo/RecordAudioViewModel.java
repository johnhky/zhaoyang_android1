package com.doctor.sun.vo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PopupAudioBinding;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NimMsgInfo;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;

import java.io.File;

/**
 * Created by rick on 14/4/2016.
 */
public class RecordAudioViewModel extends BaseObservable implements IAudioRecordCallback {
    public static final String TAG = RecordAudioViewModel.class.getSimpleName();
    public static final int IDLE = 0;
    public static final int STARTED = 1;
    public static final int ABOUT_TO_CANCEL = 2;

    private static int DIALOG_HEIGHT = 0;

    private int status = IDLE;

    private Activity activity;
    private AudioRecorder audioMessageHelper;
    private Dialog dialog;

    public RecordAudioViewModel(Context context) {
        activity = (Activity) context;
        DIALOG_HEIGHT = context.getResources().getDimensionPixelSize(R.dimen.dp_195);
    }

    public View.OnTouchListener controller() {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onStartAudioRecord();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL
                        || event.getAction() == MotionEvent.ACTION_UP) {
                    onEndAudioRecord(isCancelled(v, event));
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    onControlAudioRecord(v, event);
                }
                return true;
            }
        };
    }

    private void onControlAudioRecord(View v, MotionEvent event) {
        if (isCancelled(v, event)) {
            setStatus(ABOUT_TO_CANCEL);
        } else {
            setStatus(STARTED);
        }
    }


    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - DIALOG_HEIGHT) {
            return true;
        }

        return false;
    }


    public AudioRecorder getAudioRecorder() {
        if (audioMessageHelper == null) {
            //TODO callback
            audioMessageHelper = new AudioRecorder(activity, RecordType.AAC, AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND, this);
        }
        return audioMessageHelper;
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getAudioRecorder().startRecord();

        getDialog().show();
    }

    private Dialog getDialog() {
        if (dialog == null) {
            final PopupAudioBinding inflate = PopupAudioBinding.inflate(LayoutInflater.from(activity));
            inflate.setData(this);
            dialog = new Dialog(activity, R.style.Translucent_NoTitle);
            dialog.setContentView(inflate.getRoot());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    inflate.time.setBase(SystemClock.elapsedRealtime());
                    inflate.time.start();
                }
            });

        }
        return dialog;
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        setStatus(IDLE);
        activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getAudioRecorder().completeRecord(cancel);
        getDialog().dismiss();
    }

    public int getPopLabelBg() {
        switch (status) {
            case IDLE: {
                return R.color.transparent;
            }
            case STARTED: {
                return R.color.transparent;
            }
            case ABOUT_TO_CANCEL: {
                return R.drawable.bg_dialog_cancel;
            }
            default:
                return R.color.transparent;
        }
    }

    public String getPopLabel() {
        switch (status) {
            case IDLE: {
                return "按住说话";
            }
            case STARTED: {
                return "上滑取消发送";
            }
            case ABOUT_TO_CANCEL: {
                return "松开手指取消发送";
            }
            default:
                return "上滑取消发送";
        }
    }

    public String getStatusLabel() {
        switch (status) {
            case IDLE: {
                return "按住说话";
            }
            case STARTED: {
                return "松开手指发送语音";
            }
            case ABOUT_TO_CANCEL: {
                return "松开手指取消发送";
            }
            default:
                return "按住说话";
        }
    }

    public void setStatus(int status) {
        this.status = status;
        notifyChange();
    }

    public boolean isSelected() {
        return status != IDLE;
    }

    @Override
    public void onRecordReady() {
    }

    @Override
    public void onRecordStart(File file, RecordType recordType) {
    }

    @Override
    public void onRecordSuccess(File file, long l, RecordType recordType) {
        NimMsgInfo id = (NimMsgInfo) activity;
        IMManager.getInstance().sentAudio(id.getTeamId(), id.getType(), file, l, id.enablePush());
    }

    @Override
    public void onRecordFail() {
    }

    @Override
    public void onRecordCancel() {
    }

    @Override
    public void onRecordReachedMaxTime(int i) {
    }
}
