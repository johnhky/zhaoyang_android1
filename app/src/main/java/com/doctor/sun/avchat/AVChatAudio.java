package com.doctor.sun.avchat;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.avchat.constant.CallStateEnum;
import com.doctor.sun.avchat.widgets.ToggleListener;
import com.doctor.sun.avchat.widgets.ToggleState;
import com.doctor.sun.avchat.widgets.ToggleView;
import com.doctor.sun.entity.CallConfig;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.cache.NimUserInfoCache;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.binding.CustomBinding;
import com.doctor.sun.ui.widget.BezelImageView;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import io.ganguo.library.util.Networks;
import io.ganguo.library.util.Tasks;


/**
 * 音频管理器， 音频界面初始化和管理
 * Created by hzxuwen on 2015/4/24.
 */
public class AVChatAudio implements View.OnClickListener, ToggleListener {
    // constant
    private static final int[] NETWORK_GRADE_DRAWABLE = new int[]{R.drawable.network_grade_0, R.drawable.network_grade_1, R.drawable.network_grade_2, R.drawable.network_grade_3};
    private static final int[] NETWORK_GRADE_LABEL = new int[]{R.string.avchat_network_grade_0, R.string.avchat_network_grade_1, R.string.avchat_network_grade_2, R.string.avchat_network_grade_3};
    public static final boolean OUTGOING_CALL = true;
    public static final boolean INCOMING_CALL = false;

    // view
    private View rootView;
    private View switchVideo;
    private BezelImageView headImg;
    private TextView nickNameTV;
    private Chronometer time;
    private TextView wifiUnavailableNotifyTV;
    private TextView notifyTV;
    private TextView netUnstableTV;

    private View mute_speaker_hangup;
    private ToggleView muteToggle;
    private ToggleView speakerToggle;
    private ToggleView recordToggle;
    private View hangup;

    private View refuse_receive;
    private TextView refuseTV;
    private TextView receiveTV;

    //record
    private View recordView;
    private View recordTip;
    private View recordWarning;

    // data
    private AVChatUI manager;
    private AVChatUIListener listener;
    private final int duration;
    private boolean callDirection = INCOMING_CALL;

    // state
    private boolean init = false;
    private TextView notifyTV2;


    public AVChatAudio(View root, AVChatUIListener listener, AVChatUI manager, int duration) {
        this.rootView = root;
        this.listener = listener;
        this.manager = manager;
        this.duration = duration;
    }

    /**
     * 音视频状态变化及界面刷新
     *
     * @param state
     */
    public void onCallStateChange(CallStateEnum state) {
        if (CallStateEnum.isAudioMode(state))
            findViews();
        switch (state) {
            case OUTGOING_AUDIO_CALLING: //拨打出的免费通话
                callDirection = OUTGOING_CALL;
                setSwitchVideo(false);
                showProfile();//对方的详细信息
                showPhoneCallText();

                if (Settings.isDoctor()) {
                    hideSubNotify();
                } else {
                    showSubNotify("您本次通话最长" + duration + "分钟，自对方接通后算起，计时结束系统将自动挂断");
                }
                setWifiUnavailableNotifyTV(true);
                setMuteSpeakerHangupControl(true);
                setRefuseReceive(false);
                break;
            case INCOMING_AUDIO_CALLING://免费通话请求
                callDirection = INCOMING_CALL;
                setSwitchVideo(false);
                showProfile();//对方的详细信息
                showNotify(R.string.avchat_audio_call_request);
                if (Settings.isDoctor()) {
                    showSubNotify("本次患者发起通话时长有限，自双方接通后算起，计时结束系统将自动挂断");
                } else {
                    hideSubNotify();
                }
                setMuteSpeakerHangupControl(false);
                setRefuseReceive(true);
                receiveTV.setText(R.string.avchat_pickup);
                break;
            case AUDIO:
                setWifiUnavailableNotifyTV(false);
                showNetworkCondition(1);
                showProfile();
                setSwitchVideo(true);
                setTime(true);
                hideNotify();
                hideSubNotify();
                if (callDirection == OUTGOING_CALL) {
                    if (!Settings.isDoctor()) {
                        hangupIn(duration);
                    }
                }
                setMuteSpeakerHangupControl(true);
                setRefuseReceive(false);
                enableToggle();
                break;
            case AUDIO_CONNECTING:
                showNotify(R.string.avchat_connecting);
                hideSubNotify();
                break;
            case INCOMING_AUDIO_TO_VIDEO:
                showNotify(R.string.avchat_audio_to_video_invitation);
                setMuteSpeakerHangupControl(false);
                setRefuseReceive(true);
                receiveTV.setText(R.string.avchat_receive);
                break;
            default:
                break;
        }
        setRoot(CallStateEnum.isAudioMode(state));
    }


    private boolean isEnabled = false;

    private void enableToggle() {
        if (!isEnabled) {
            recordToggle.enable();
        }
        isEnabled = true;
    }

    private void showPhoneCallText() {
        ToolModule api = Api.of(ToolModule.class);
        api.getCallConfig().enqueue(new SimpleCallback<CallConfig>() {
            @Override
            protected void handleResponse(CallConfig response) {
                notifyTV.setText(response.getPhoneCallText());
                notifyTV.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 界面初始化
     */
    private void findViews() {
        if (init || rootView == null) {
            return;
        }
        switchVideo = rootView.findViewById(R.id.avchat_audio_switch_video);
        switchVideo.setOnClickListener(this);

        headImg = (BezelImageView) rootView.findViewById(R.id.avchat_audio_head);
        nickNameTV = (TextView) rootView.findViewById(R.id.avchat_audio_nickname);
        time = (Chronometer) rootView.findViewById(R.id.avchat_audio_time);
        wifiUnavailableNotifyTV = (TextView) rootView.findViewById(R.id.avchat_audio_wifi_unavailable);
        notifyTV = (TextView) rootView.findViewById(R.id.avchat_audio_notify);
        notifyTV2 = (TextView) rootView.findViewById(R.id.avchat_audio_notify_second);
        netUnstableTV = (TextView) rootView.findViewById(R.id.avchat_audio_netunstable);

        mute_speaker_hangup = rootView.findViewById(R.id.avchat_audio_mute_speaker_huangup);
        View mute = mute_speaker_hangup.findViewById(R.id.avchat_audio_mute);
        muteToggle = new ToggleView(mute, ToggleState.OFF, this);
        View speaker = mute_speaker_hangup.findViewById(R.id.avchat_audio_speaker);
        speakerToggle = new ToggleView(speaker, ToggleState.OFF, this);
        View record = mute_speaker_hangup.findViewById(R.id.avchat_audio_record);
        recordToggle = new ToggleView(record, ToggleState.OFF, this);
        hangup = mute_speaker_hangup.findViewById(R.id.avchat_audio_hangup);
        hangup.setOnClickListener(this);

        recordToggle.disable(false);

        refuse_receive = rootView.findViewById(R.id.avchat_audio_refuse_receive);
        refuseTV = (TextView) refuse_receive.findViewById(R.id.refuse);
        receiveTV = (TextView) refuse_receive.findViewById(R.id.receive);
        refuseTV.setOnClickListener(this);
        receiveTV.setOnClickListener(this);

        recordView = rootView.findViewById(R.id.avchat_record_layout);
        recordTip = rootView.findViewById(R.id.avchat_record_tip);
        recordWarning = rootView.findViewById(R.id.avchat_record_warning);

        init = true;
    }

    /**
     * ********************************* 界面设置 *************************************
     */

    /**
     * 个人信息设置
     */
    private void showProfile() {
        String account = manager.getAccount();
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo nimUserInfo) {
                nickNameTV.setText(nimUserInfo.getName());
                CustomBinding.loadAvatar(headImg,nimUserInfo.getAvatar(),0);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    /**
     * 界面状态文案设置
     *
     * @param resId 文案
     */
    private void showNotify(int resId) {
        notifyTV.setText(resId);
        notifyTV.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏界面文案
     */
    private void hideNotify() {
        notifyTV.setVisibility(View.GONE);
    }

    private void showSubNotify(String string) {
        notifyTV2.setText(string);
        notifyTV2.setVisibility(View.VISIBLE);
    }

    private void showSubNotify(int resId) {
        notifyTV2.setText(resId);
        notifyTV2.setVisibility(View.VISIBLE);
    }

    private void hideSubNotify() {
        notifyTV2.setVisibility(View.GONE);
    }

    public void showRecordView(boolean show, boolean warning) {
        if (show) {
            recordView.setVisibility(View.VISIBLE);
            recordTip.setVisibility(View.VISIBLE);
            if (warning) {
                recordWarning.setVisibility(View.VISIBLE);
            } else {
                recordWarning.setVisibility(View.GONE);
            }
        } else {
            recordView.setVisibility(View.INVISIBLE);
            recordTip.setVisibility(View.INVISIBLE);
            recordWarning.setVisibility(View.GONE);
        }
    }

    /**
     * 显示网络状态
     *
     * @param grade
     */
    public void showNetworkCondition(int grade) {
        if (grade >= 0 && grade < NETWORK_GRADE_DRAWABLE.length) {
            netUnstableTV.setText(NETWORK_GRADE_LABEL[grade]);
            Drawable drawable = AppContext.me().getResources().getDrawable(NETWORK_GRADE_DRAWABLE[grade]);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                netUnstableTV.setCompoundDrawables(null, null, drawable, null);
            }
            netUnstableTV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ***************************** 布局显隐设置 ***********************************
     */

    private void setRoot(boolean visible) {
        rootView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或隐藏音视频切换
     *
     * @param visible
     */
    private void setSwitchVideo(boolean visible) {
        switchVideo.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或者隐藏是否为wifi的提示
     *
     * @param show
     */
    private void setWifiUnavailableNotifyTV(boolean show) {
        if (show && !Networks.isWifi(AppContext.me())) {
            wifiUnavailableNotifyTV.setVisibility(View.VISIBLE);
        } else {
            wifiUnavailableNotifyTV.setVisibility(View.GONE);
        }
    }

    /**
     * 显示或隐藏禁音，结束通话布局
     *
     * @param visible
     */
    private void setMuteSpeakerHangupControl(boolean visible) {
        mute_speaker_hangup.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 显示或隐藏拒绝，开启布局
     *
     * @param visible
     */
    private void setRefuseReceive(boolean visible) {
        refuse_receive.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置通话时间显示
     *
     * @param visible
     */
    private void setTime(boolean visible) {
        time.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible) {
            time.setBase(manager.getTimeBase());
            time.start();
        }
    }

    /**
     * 视频切换为音频时，禁音与扬声器按钮状态
     *
     * @param muteOn
     * @param speakerOn
     */
    public void onVideoToAudio(boolean muteOn, boolean speakerOn, boolean recordOn, boolean recordWarning) {

        muteToggle.toggle(muteOn ? ToggleState.ON : ToggleState.OFF);
        speakerToggle.toggle(speakerOn ? ToggleState.ON : ToggleState.OFF);
        recordToggle.toggle(recordOn ? ToggleState.ON : ToggleState.OFF);

        showRecordView(recordOn, recordWarning);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avchat_audio_hangup:
                listener.onHangUp();
                break;
            case R.id.refuse:
                listener.onRefuse();
                break;
            case R.id.receive:
                listener.onReceive();
                break;
            case R.id.avchat_audio_mute:
                listener.toggleMute();
                break;
            case R.id.avchat_audio_speaker:
                listener.toggleSpeaker();
                break;
            case R.id.avchat_audio_switch_video:
                listener.audioSwitchVideo();
                break;
            case R.id.avchat_audio_record:
                listener.toggleRecord();
                break;
            default:
                break;
        }
    }

    void closeSession(int exitCode) {
        if (init) {
            time.stop();
            muteToggle.disable(false);
            speakerToggle.disable(false);
            recordToggle.disable(false);
            refuseTV.setEnabled(false);
            receiveTV.setEnabled(false);
            hangup.setEnabled(false);
        }
    }


    private void hangupIn(int durationMinutes) {
        if (durationMinutes <= 0) {
            return;
        }
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onHangUp();
            }
        }, durationMinutes * 60000);
    }

    /*******************************
     * toggle listener
     *************************/
    @Override
    public void toggleOn(View v) {
        onClick(v);
    }

    @Override
    public void toggleOff(View v) {
        onClick(v);
    }

    @Override
    public void toggleDisable(View v) {

    }
}
