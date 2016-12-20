package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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


    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_patient, R.layout.item_patient_contact);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        String string = getArguments().getString(Constants.TYPE);
        api.newPatientList("", string).enqueue(new SimpleCallback<PageDTO<Patient>>() {
            @Override
            protected void handleResponse(PageDTO<Patient> response) {
                getAdapter().clear();
                getAdapter().addAll(response.getData());
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                updateEmptyIndicator();
            }
        });

        // getEmptyIndicatorText 无效。。。

    }

    public void updateEmptyIndicator() {
        if (getAdapter().size() == 0) {
            String type = getArguments().getString(Constants.TYPE);
            String text;
            if ("follow".equals(type)) {
                text = "暂无已建立随访关系的患者";
            } else {
                text = "暂无未建立随访关系的患者";
            }
            binding.emptyIndicator.setText(text);
            binding.emptyIndicator.setVisibility(View.VISIBLE);
        }
    }
}
