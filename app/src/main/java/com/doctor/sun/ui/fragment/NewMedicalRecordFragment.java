package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.event.SelectMedicalRecordEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.NewMedicalRecordModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordFragment extends SortedListFragment {

    public static final String TAG = NewMedicalRecordFragment.class.getSimpleName();

    public static final int TYPE_SELF = 0;
    public static final int TYPE_OTHER = 1;

    private NewMedicalRecordModel model;

    public static Bundle getArgs(int recordType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.RECORD_TYPE, recordType);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new NewMedicalRecordModel(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableRefresh();
        setHasOptionsMenu(true);
        List<SortedItem> sortedItems = model.parseItem(getRecordType());
        getAdapter().insertAll(sortedItems);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_confirm, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm: {
                saveMedicalRecord();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRecordType() {
        return getArguments().getInt(Constants.RECORD_TYPE);
    }

    private void saveMedicalRecord() {
        model.saveMedicalRecord(getAdapter(), getRecordType(), new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(getContext(), "病历创建成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), RecordListActivity.class);
//                getContext().startActivity(intent);
                EventHub.post(new SelectMedicalRecordEvent(getArguments().getString(Constants.FROM), response));
                getActivity().finish();
            }
        });

    }

}
