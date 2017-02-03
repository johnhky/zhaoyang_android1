package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.fragment.BottomSheetTabFragment;
import com.doctor.sun.ui.pager.DoctorAppointmentDonePA;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 18/10/2016.
 */

public class AppointmentHistoryDialog extends BottomSheetTabFragment implements View.OnClickListener {
    public static final String TAG = AppointmentHistoryDialog.class.getSimpleName();

    public static final String HISTORY_INDEX = "HISTORY_INDEX";
    private DoctorAppointmentDonePA answerPagerAdapter;

    DiagnosisModule api = Api.of(DiagnosisModule.class);

    private Appointment appointment;
    private String recordId;
    private List<Appointment> data = new ArrayList<>();
    private int currentIndex = 0;

    public static AppointmentHistoryDialog newInstance(Appointment data) {
        AppointmentHistoryDialog fragment = new AppointmentHistoryDialog();
        Bundle bundle = new Bundle();

        bundle.putString(Constants.DATA, JacksonUtils.toJson(data));
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = JacksonUtils.fromJson(getArguments().getString(Constants.DATA), Appointment.class);
        if (appointment != null) {
            recordId = appointment.getRecord_id();
            currentIndex = Config.getInt(HISTORY_INDEX + appointment.getId(), 0);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().toolBar.setNavigationOnClickListener(this);
        getBinding().tvPrevious.setOnClickListener(this);
        getBinding().tvNext.setOnClickListener(this);
        getBinding().tbMenu.setOnClickListener(this);

        api.recordHistory(appointment.getRecord().getMedicalRecordId()).enqueue(new SimpleCallback<List<Appointment>>() {
            @Override
            protected void handleResponse(List<Appointment> response) {
                data.addAll(response);
                if (data.isEmpty()) {
                    Toast.makeText(getContext(), "暂时没有任何历史记录", Toast.LENGTH_SHORT).show();
                } else {
                    toggleVisibility();

                    initPagerAdapter();

                    initPagerTabs();

                    setCurrentItem();
                }
            }
        });
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (currentIndex > data.size() || currentIndex < 0) {
            currentIndex = 0;
        }

        String id;
        if (data.isEmpty()) {
            id = "";
        } else {
            id = data.get(currentIndex).getId();
        }
        answerPagerAdapter = new DoctorAppointmentDonePA(getChildFragmentManager(), id);
        return answerPagerAdapter;
    }

    private void toggleVisibility() {
        getBinding().tvNext.setVisibility(View.VISIBLE);
        getBinding().tvPrevious.setVisibility(View.VISIBLE);

        if (currentIndex >= data.size() - 1) {
            getBinding().tvNext.setVisibility(View.GONE);
        }

        if (currentIndex == 0) {
            getBinding().tvPrevious.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setIndex(currentIndex, appointment.getId());
    }

    public static void setIndex(int index, String id) {
        Config.putInt(HISTORY_INDEX + id, index);
    }

    public static int getIndex(String id) {
        return Config.getInt(HISTORY_INDEX + id, 0);
    }

    @Override
    public void onClick(View view) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(getContext(), "暂时没有任何历史记录", Toast.LENGTH_SHORT).show();
            return;
        }
        setTabPosition();

        switch (view.getId()) {
            case R.id.tv_previous: {
                currentIndex -= 1;
                setIndex(currentIndex, appointment.getId());
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
                setCurrentItem();
                break;
            }
            case R.id.tv_next: {
                currentIndex += 1;
                setIndex(currentIndex, appointment.getId());
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
                setCurrentItem();
                break;
            }
            case R.id.tb_menu: {
                dismiss();
                if (data != null && !data.isEmpty()) {
                    EventHub.post(new AppointmentHistoryEvent(appointment, true));
                }
                break;
            }
            default: {
                dismiss();
                break;
            }
        }
    }

    @Override
    public int getPosition() {
        return getTabPosition();
    }

    private SharedPreferences getSharedPref() {
        BaseApp me = AppContext.me();
        return me.getSharedPreferences("APPOINTMENT_HISTORY", Context.MODE_PRIVATE);
    }

    private void setTabPosition() {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(recordId + Constants.TAB_POSITION, getBinding().bottomSheetViewpager.getCurrentItem());
        editor.apply();
    }

    private int getTabPosition() {
        return getSharedPref().getInt(recordId + Constants.TAB_POSITION, 1);
    }

    @Override
    protected String getTitle() {
        try {
            return data.get(currentIndex).getTime_bucket().split(" ")[0];
        } catch (Exception e) {
            return "";
        }
    }
}
