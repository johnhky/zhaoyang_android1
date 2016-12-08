package com.doctor.sun.ui.activity.doctor;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPatientDetailBinding;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.event.CallFailedShouldCallPhoneEvent;
import com.doctor.sun.event.FinishRefreshEvent;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.util.HistoryEventHandler;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.vm.ItemPatientDetail;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;


/**
 * Created by rick on 11/24/15.
 */
public class PatientDetailActivity extends BaseFragmentActivity2 {

    private ActivityPatientDetailBinding binding;
    private Appointment data;
    private ReadQuestionsFragment instance;
    private HistoryEventHandler eventHandler;
    private boolean animating;
    private View fab;

    public static Intent makeIntent(Context context, Appointment data, int layout) {
        Intent i = new Intent(context, PatientDetailActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        i.putExtra(Constants.LAYOUT_ID, layout);
        return i;
    }

    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_detail);

        data = getData();

        binding.setData(data);
        instance = ReadQuestionsFragment.getInstance(data.getId(), QuestionsPath.NORMAL, false);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, instance)
                .commit();

        addHistoryButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventHandler = HistoryEventHandler.register(getSupportFragmentManager());
    }

    @Override
    protected void onPause() {
        super.onPause();
        HistoryEventHandler.unregister(eventHandler);
    }

    private void addHistoryButton() {
        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_history_button, binding.flyContent, true);
            fab = historyButton.findViewById(R.id.btn_appointment_history);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventHub.post(new AppointmentHistoryEvent(getData(), false));
                }
            });
        }
    }


    public void onMenuClicked() {
        String appointmentId = getData().getId();
        Intent intent = TemplatesInventoryActivity.intentFor(this, appointmentId);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppointmentHandler2.RECORD_AUDIO_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                AppointmentHandler2.makePhoneCall(this, data);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Subscribe
    public void onRejectIncomingCallEvent(CallFailedShouldCallPhoneEvent e) {
        if (data != null) {
            if (AVChatType.AUDIO.getValue() == e.getChatType()) {
                AppointmentHandler2.callTelephone(this, getData());
            }
        }
    }

    @Subscribe
    public void onEventMainThread(FinishRefreshEvent e) {
        if (instance == null) {
            return;
        }
        ItemPatientDetail item = new ItemPatientDetail(R.layout.include_patient_detail, data);
        item.setItemId("patient_detail");
        item.setPosition(Long.MAX_VALUE - 1);
        SortedListAdapter adapter = instance.getAdapter();
        if (adapter != null) {
            adapter.insert(item);
        }
    }

    @Subscribe
    public void onEventMainThread(HideFABEvent event) {
        if (fab == null || animating) {
            return;
        }
        fab.animate()
                .translationY(fab.getHeight())
                .alpha(0)
                .setDuration(250)
                .setListener(getAnimationListener())
                .setInterpolator(new DecelerateInterpolator());

    }

    @Subscribe
    public void onEventMainThread(ShowFABEvent event) {
        if (fab == null || animating) {
            return;
        }
        fab.animate()
                .translationY(0)
                .alpha(1)
                .setDuration(250)
                .setListener(getAnimationListener())
                .setInterpolator(new DecelerateInterpolator());
    }

    @NonNull
    public Animator.AnimatorListener getAnimationListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }
}
