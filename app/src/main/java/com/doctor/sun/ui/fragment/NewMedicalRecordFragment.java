package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.model.AddSelfMedicalRecordModel;
import com.doctor.sun.ui.activity.patient.RecordListActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.List;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordFragment extends SortedListFragment{

    public static final String TAG = NewMedicalRecordFragment.class.getSimpleName();

    private AddSelfMedicalRecordModel model;

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, "NewMedicalRecordFragment");

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new AddSelfMedicalRecordModel(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableRefresh();
        setHasOptionsMenu(true);
        List<SortedItem> sortedItems = model.parseItem();
        getAdapter().insertAll(sortedItems);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish: {
                saveMedicalRecord();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveMedicalRecord() {
        model.saveMedicalRecord(getAdapter(), new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                TokenCallback.checkToken(getActivity());
                Toast.makeText(getContext(), "病历创建成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), RecordListActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
}
