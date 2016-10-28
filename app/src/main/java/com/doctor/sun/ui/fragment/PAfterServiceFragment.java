package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/6/2016.
 */
public class PAfterServiceFragment extends RefreshListFragment {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);


    public static PAfterServiceFragment newInstance() {

        Bundle args = new Bundle();

        PAfterServiceFragment fragment = new PAfterServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.patientOrders("", getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_after_service);
        return adapter;
    }


    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(getContext(), ContactActivity.PATIENTS_CONTACT);
        startActivity(intent);
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何随访请求";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_doctor_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_doctor_list: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
