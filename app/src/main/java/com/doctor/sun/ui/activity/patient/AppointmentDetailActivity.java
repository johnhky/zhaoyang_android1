package com.doctor.sun.ui.activity.patient;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ImportDiagnosisEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;
import com.doctor.sun.ui.pager.ReadAnswerPagerAdapter;
import com.doctor.sun.util.HistoryEventHandler;
import com.doctor.sun.util.JacksonUtils;
import com.google.common.base.Strings;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 1/8/2016.
 */

public class AppointmentDetailActivity extends TabActivity {
    public static final int POSITION_ANSWER = 0;
    public static final int POSITION_SUGGESTION = 1;
    public static final int POSITION_SUGGESTION_READONLY = 2;
    public static boolean onlyRead;


    private HistoryEventHandler eventHandler;
    private View historyButton;
    private boolean animating;

    public static Intent makeIntent(Context context, Appointment data, int position) {
        Intent i = intentFor(context, data);
        i.putExtra(Constants.POSITION, position);
        onlyRead=true;
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setData(getData());
        addHistoryButton();
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        Tasks.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (hasImportDiagnosisArgs()) {
//                    resendImportDiagnosisEvent();
//                }
//            }
//        }, 200);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        eventHandler = HistoryEventHandler.register(getSupportFragmentManager());
    }

    private void addHistoryButton() {
        if (Settings.isDoctor()) {
            historyButton = LayoutInflater.from(this).inflate(R.layout.item_fab_view_history, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appointment appointment = getData();
                    EventHub.post(new AppointmentHistoryEvent(appointment, false));
                }
            });
            binding.flContainer.addView(historyButton);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        HistoryEventHandler.unregister(eventHandler);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        Appointment data = getData();
        if(onlyRead){
            return new ReadAnswerPagerAdapter(getSupportFragmentManager(), data);
        }
        return new AnswerPagerAdapter(getSupportFragmentManager(), data);
    }

    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EventHub.post(new ActivityResultEvent(requestCode, resultCode, data));
    }

    public static Intent intentFor(Context context, Appointment data) {
        Intent intent = new Intent(context, AppointmentDetailActivity.class);
        intent.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return intent;
    }

    @com.squareup.otto.Subscribe
    public void onEventMainThread(HideFABEvent event) {
        if (historyButton == null || animating) {
            return;
        }
        historyButton.animate()
                .translationY(historyButton.getHeight())
                .alpha(0)
                .setDuration(250)
                .setListener(getAnimationListener())
                .setInterpolator(new DecelerateInterpolator());

    }

    @com.squareup.otto.Subscribe
    public void onEventMainThread(ShowFABEvent event) {
        if (historyButton == null || animating) {
            return;
        }
        historyButton.animate()
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

//    public boolean hasImportDiagnosisArgs() {
//        boolean hasImportId = !Strings.isNullOrEmpty(getImportId());
//        boolean hasImportType = getImportType() != -1;
//        return hasImportId && hasImportType;
//    }
//
//    public int getImportType() {
//        return getIntent().getIntExtra(Constants.IMPORT_TYPE, -1);
//    }
//
//    public String getImportId() {
//        return getIntent().getStringExtra(Constants.IMPORT_ID);
//    }
//
//    public void resendImportDiagnosisEvent() {
//        ImportDiagnosisEvent event = new ImportDiagnosisEvent(getData().getId(), getImportId(),getImportType());
//        EventHub.post(event);
//    }

}
