package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.DiagnosisReadOnlyViewModel;
import com.doctor.sun.vo.ItemTextInput;

/**
 * Created by rick on 29/6/2016.
 * 就诊医生建议只读
 */
public class ReadDiagnosisFragment extends RefreshListFragment {
    private DiagnosisModule api = Api.of(DiagnosisModule.class);
    private DiagnosisReadOnlyViewModel viewModel;


    public static ReadDiagnosisFragment newInstance(int appointmentId) {

        Bundle args = new Bundle();
        args.putInt(Constants.DATA, appointmentId);

        ReadDiagnosisFragment fragment = new ReadDiagnosisFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getAppointmentId() {
        return getArguments().getInt(Constants.DATA, 0);
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.diagnosisInfo(getAppointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                if (response == null) {
                    Description divider = new Description(R.layout.item_description, "医嘱");
                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                    textInput.setInput("坚持用药，定期复诊");
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
            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {

        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_symptom, R.layout.item_symptom_readonly);
        adapter.mapLayout(R.layout.item_diagnosis, R.layout.item_consultation_readonly);
        adapter.mapLayout(R.layout.item_prescription, R.layout.item_prescription2);
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_transfer_doctor);
        return adapter;
    }
}
