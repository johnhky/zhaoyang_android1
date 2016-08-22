package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.DoctorInfoModel;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vo.ItemPickImage;

import java.util.List;

/**
 * Created by rick on 28/7/2016.
 */

public class EditDoctorInfoFragment extends SortedListFragment {
    public static final String NAME = "EditDoctorInfoFragment";

    private DoctorInfoModel model;
    private Doctor data;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_NAME, NAME);
        i.putExtra(Constants.DATA, doctor);
        return i;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new DoctorInfoModel();
        data = getArguments().getParcelable(Constants.DATA);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<SortedItem> sortedItems = model.parseData(data);
        getAdapter().insertAll(sortedItems);
    }

    public void save() {
        model.postResult(getAdapter(), new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Log.e(TAG, "handleResponse: " + response);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish: {
                save();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ItemPickImage.handleRequest(getActivity(), getAdapter(), data, requestCode);
        }
    }

    @Override
    public void onRefresh() {
        getBinding().swipeRefresh.setRefreshing(false);
    }

}