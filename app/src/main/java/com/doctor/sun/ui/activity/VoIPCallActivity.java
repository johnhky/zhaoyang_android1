package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityVoipCallBinding;
import com.doctor.sun.entity.Avatar;
import com.doctor.sun.event.CallAlertEvent;
import com.doctor.sun.event.CallAnsweredEvent;
import com.doctor.sun.event.CallFailedEvent;
import com.doctor.sun.event.CallReleasedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.squareup.otto.Subscribe;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECVoIPCallManager;


/**
 * Created by rick on 1/2/2016.
 */
public class VoIPCallActivity extends BaseActivity2 {
    public static final int THREE_SECOND = 1500;
    public static final int CALLING = 1;

    private ImModule api = Api.of(ImModule.class);
    private ActivityVoipCallBinding binding;
    protected ECVoIPCallManager.CallType mCallType;
    private String mCallId;
    private String mCallNumber = "";
    private Handler handler;
    private Counter counter;


    public static Intent makeIntent(Context context, int type, String sendTo) {
        Intent i = new Intent(context, VoIPCallActivity.class);
        i.putExtra(Constants.TYPE, type);
        i.putExtra(ECDevice.CALLER, sendTo);
        return i;
    }

    public int getType() {
        int result = getIntent().getIntExtra(Constants.TYPE, -1);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_voip_call);
        initCallInfo();
        initListener();
    }

    private void initCallInfo() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }
        mCallType = (ECVoIPCallManager.CallType)
                getIntent().getSerializableExtra(ECDevice.CALLTYPE);
        mCallId = getIntent().getStringExtra(ECDevice.CALLID);
        mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);

        api.avatar(mCallNumber, "").enqueue(new SimpleCallback<Avatar>() {
            @Override
            protected void handleResponse(Avatar avatar) {
                binding.setData(avatar);
                if (getType() == CALLING) {
                    binding.status.setText("呼叫中...");
                } else {
                    askIfAcceptCall(avatar);
                }
            }
        });
    }

    private void askIfAcceptCall(Avatar avatar) {
        TwoSelectorDialog.showDialog(this, avatar.getName() + "来电", "拒绝", "接受", new TwoSelectorDialog.GetActionButton() {
            @Override
            public void onClickPositiveButton(TwoSelectorDialog dialog) {
                ECDevice.getECVoIPCallManager()
                        .acceptCall(mCallId);
                dialog.dismiss();
            }

            @Override
            public void onClickNegativeButton(TwoSelectorDialog dialog) {
                ECDevice.getECVoIPCallManager()
                        .rejectCall(mCallId, 1);
                ECDevice.getECVoIPCallManager().releaseCall(mCallId);
                dialog.dismiss();
                finish();
            }
        });
    }

    private void initListener() {
        binding.tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECDevice.getECVoIPCallManager()
                        .releaseCall(mCallId);
                if (counter == null) {
                    finishDelay();
                }
            }
        });
    }

    @Subscribe
    public void onCallAlert(CallAlertEvent event) {
        if (mCallNumber.equals("")) {
            ECVoIPCallManager.VoIPCall voIPCall = event.getVoIPCall();
            updateVopiCall(voIPCall);
            api.avatar(mCallNumber, "").enqueue(new SimpleCallback<Avatar>() {
                @Override
                protected void handleResponse(Avatar avatar) {
                    binding.setData(avatar);
                    if (getType() == CALLING) {
                        binding.status.setText("呼叫中...");
                    } else {
                        askIfAcceptCall(avatar);
                    }
                }
            });
        }
    }

    private void updateVopiCall(ECVoIPCallManager.VoIPCall voIPCall) {
        mCallNumber = voIPCall.caller;
        mCallId = voIPCall.callId;
        mCallType = voIPCall.callType;
    }

    @Subscribe
    public void onCallAnswered(CallAnsweredEvent event) {
        updateVopiCall(event.getVoIPCall());
        counter = new Counter();
        handler.post(counter);
    }

    @Subscribe
    public void onCallReleased(CallReleasedEvent event) {
        updateVopiCall(event.getVoIPCall());
        ECDevice.getECVoIPCallManager()
                .releaseCall(mCallId);
        if (counter != null) {
            counter.stop();
        } else {
            finishDelay();
        }
    }

    @Subscribe
    public void onCallFailed(CallFailedEvent event) {
        updateVopiCall(event.getVoIPCall());
        binding.status.setText("您拨叫的用户正忙,请稍后再拨");
        ECDevice.getECVoIPCallManager()
                .releaseCall(mCallId);
        finishDelay();
    }

    private class Counter implements Runnable {
        public static final int ONE_SECOND = 1000;
        public static final int ONE_MINUTE = 60000;

        private long time = 0;

        private boolean isRunning = true;

        @Override
        public void run() {
            if (isRunning) {
                time += 1000;
                binding.status.setText(getTime(time));
                handler.postDelayed(this, ONE_SECOND);
            }
        }

        public String getTime(long time) {
            long minute = time / ONE_MINUTE;
            long second = (time % ONE_MINUTE) / ONE_SECOND;
            return String.format("%02d:%02d", minute, second);
        }

        public void stop() {
            isRunning = false;
            handler.removeCallbacks(this);
            binding.status.setText("通话结束");
            finishDelay();
        }


    }

    private void finishDelay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, THREE_SECOND);
    }


}
