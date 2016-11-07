package com.doctor.sun.ui.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.DismissHistoryListDialogEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 18/10/2016.
 */

public class HistoryListDialog extends BottomSheetListFragment {
    public static final String TAG = HistoryListDialog.class.getSimpleName();
    public static final String HAVE_SHOW = "HAVE_SHOW";

    DiagnosisModule api = Api.of(DiagnosisModule.class);
    private int recordId;


    public static HistoryListDialog newInstance(int recordId) {
        HistoryListDialog fragment = new HistoryListDialog();
        Bundle bundle = new Bundle();

        bundle.putInt(Constants.DATA, recordId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordId = getArguments().getInt(Constants.DATA);
        EventHub.register(this);
        setHaveShown(recordId);
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        api.recordHistoryDetail(recordId).enqueue(new SimpleCallback<List<Appointment>>() {
            @Override
            protected void handleResponse(List<Appointment> response) {
                getAdapter().insertAll(response);
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.item_appointment_history);
        return adapter;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHub.unregister(this);
    }

    @Subscribe
    public void onEvent(DismissHistoryListDialogEvent e) {
        dismiss();
    }

    public static void setHaveShown(int recordId) {
        Config.putBoolean(HAVE_SHOW + recordId, true);
    }

    public static boolean isShowBefore(int recordId) {
        return Config.getBoolean(HAVE_SHOW + recordId, false);
    }

}
