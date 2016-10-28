package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.model.EditPrescriptionModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.BaseItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 28/7/2016.
 */

public class EditPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = EditPrescriptionsFragment.class.getSimpleName();

    private EditPrescriptionModel model;
    private Prescription data;


    public static Bundle getArgs(Prescription data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA, JacksonUtils.toJson(data));
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new EditPrescriptionModel();
        data = JacksonUtils.fromJson(getArguments().getString(Constants.DATA), Prescription.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<SortedItem> sortedItems = model.parseData(data);
        binding.swipeRefresh.setRefreshing(false);
        for (int i = 0; i < sortedItems.size(); i++) {
            BaseItem item = (BaseItem) sortedItems.get(i);
            item.setPosition(i);
            getAdapter().insert(item);
        }
    }

    public void save() {
        HashMap<String, String> save = model.save(getAdapter(), new SimpleCallback() {
            @Override
            protected void handleResponse(Object response) {
                Log.d(TAG, "handleResponse() called with: response = [" + response + "]");
            }
        });


        if (save != null) {
            Prescription data = PrescriptionHandler.fromHashMap(save);
            String jsonData = JacksonUtils.toJson(data);
            Messenger messenger = getActivity().getIntent().getParcelableExtra(Constants.HANDLER);
            if (messenger != null) {
                try {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DATA, jsonData);
                    message.setData(bundle);
                    message.what = DiagnosisFragment.EDIT_PRESCRITPION;
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = getActivity().getIntent();
            intent.putExtra(Constants.DATA, jsonData);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
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

}
