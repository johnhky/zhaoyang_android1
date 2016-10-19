package com.doctor.sun.ui.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.fragment.BottomSheetTabFragment;
import com.doctor.sun.ui.pager.AnswerPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 18/10/2016.
 */

public class AppointmentHistoryDialog extends BottomSheetTabFragment {
    private AnswerPagerAdapter answerPagerAdapter;

    DiagnosisModule api = Api.of(DiagnosisModule.class);

    private Appointment appointment;
    private List<Appointment> data = new ArrayList<>();
    private int currentIndex = 0;

    public static AppointmentHistoryDialog newInstance(Appointment data) {
        AppointmentHistoryDialog fragment = new AppointmentHistoryDialog();
        Bundle bundle = new Bundle();

        bundle.putParcelable(Constants.DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getArguments().getParcelable(Constants.DATA);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex -= 1;
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
            }
        });

        getBinding().tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex += 1;
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
            }
        });

        api.recordHistory(appointment.getRecordId(), "").enqueue(new SimpleCallback<List<Appointment>>() {
            @Override
            protected void handleResponse(List<Appointment> response) {
                data.addAll(response);

                toggleVisibility();

                initPagerAdapter();

                initPagerTabs();

                setCurrentItem();
            }
        });
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        answerPagerAdapter = new AnswerPagerAdapter(getChildFragmentManager(), data.get(currentIndex));
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
}
