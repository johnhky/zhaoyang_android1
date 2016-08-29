package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 7/6/2016.
 */
public class PAfterServiceContactFragment extends RefreshListFragment {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static PAfterServiceContactFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putString(Constants.TYPE, path);

        PAfterServiceContactFragment fragment = new PAfterServiceContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_patient, R.layout.item_patient2);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        String string = getArguments().getString(Constants.TYPE);
        api.newPatientList("", string).enqueue(new SimpleCallback<PageDTO<Patient>>() {
            @Override
            protected void handleResponse(PageDTO<Patient> response) {
                getAdapter().addAll(response.getData());
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}
