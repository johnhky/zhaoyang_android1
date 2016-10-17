package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.event.RefreshEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 1/6/2016.
 */
public class PApplyingDoctorListFragment extends RefreshListFragment {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, PApplyingDoctorListFragment.class);
        return intent;
    }

    public static PApplyingDoctorListFragment newInstance() {

        Bundle args = new Bundle();

        PApplyingDoctorListFragment fragment = new PApplyingDoctorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.applyingDoctorList("", "applying", getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_doctor_relations);
        return adapter;
    }


    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(getContext(), ContactActivity.PATIENTS_CONTACT);
//        Intent intent = ContactActivity.intentFor(this, ContactActivity.PATIENTS_CONTACT, R.layout.item_contact3);
        startActivity(intent);
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何随访关系申请";
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

    @Override
    public void onResume() {
        super.onResume();
        EventHub.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHub.unregister(this);
    }

    @Subscribe
    public void onRefreshEvent(RefreshEvent event) {
        onRefresh();
    }
}
