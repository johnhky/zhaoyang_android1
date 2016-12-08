package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.DiagnosisReadOnlyViewModel;
import com.doctor.sun.vm.ItemTextInput;

/**
 * Created by rick on 29/6/2016.
 * 就诊医生建议只读
 */
@Factory(type = BaseFragment.class, id = "ReadDiagnosisFragment")
public class ReadDiagnosisFragment extends RefreshListFragment {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private DiagnosisReadOnlyViewModel viewModel;

    public static final String TAG = ReadDiagnosisFragment.class.getSimpleName();

    public static ReadDiagnosisFragment newInstance(String appointmentId) {

        Bundle args = new Bundle();
        args.putString(Constants.DATA, appointmentId);

        ReadDiagnosisFragment fragment = new ReadDiagnosisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle getArgs(String appointmentId) {
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_NAME, TAG);
        args.putString(Constants.DATA, appointmentId);

        return args;
    }

    public String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.diagnosisInfo(getAppointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                if (response == null) {
                    Description divider = new Description(R.layout.item_description, "嘱咐");
                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                    textInput.setInput("待医生诊断");
                    getAdapter().add(divider);
                    getAdapter().add(textInput);
                    getAdapter().notifyDataSetChanged();
                    binding.swipeRefresh.setRefreshing(false);
                    return;
                }
                viewModel = new DiagnosisReadOnlyViewModel();
                viewModel.cloneFromDiagnosisInfo(response);
                getAdapter().onFinishLoadMore(true);
                getAdapter().clear();
                getAdapter().addAll(viewModel.toList());
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                if (getAdapter().isEmpty()) {
                    binding.emptyIndicator.setText("请耐心等待");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {

        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_symptom, R.layout.item_symptom_readonly);
        adapter.mapLayout(R.layout.item_symptom_single_choice, R.layout.item_symptom_readonly);
        adapter.mapLayout(R.layout.item_diagnosis, R.layout.item_consultation_readonly);
        adapter.mapLayout(R.layout.item_prescription, R.layout.item_r_prescription);
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_transfer_doctor);
        return adapter;
    }

    @Override
    public ShowFABEvent getShowFABEvent() {
        return new ShowFABEvent(getAppointmentId());
    }

    @Override
    public HideFABEvent getHideFABEvent() {
        return new HideFABEvent(getAppointmentId());
    }
}
