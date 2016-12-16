package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.event.HideKeyboardEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.ImmutablePrescription;
import com.doctor.sun.immutables.ModifiablePrescription;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.model.EditPrescriptionModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.BaseItem;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.Systems;

/**
 * Created by rick on 28/7/2016.
 */

@Factory(type = BaseFragment.class, id = "EditPrescriptionsFragment")
public class EditPrescriptionsFragment extends SortedListFragment {
    public static final String TAG = EditPrescriptionsFragment.class.getSimpleName();

    private EditPrescriptionModel model;
    private Prescription data;
    private boolean isReadOnly;


    public static Bundle getArgs(Prescription data, boolean isReadOnly) {
        Bundle bundle = new Bundle();
        if (data instanceof ModifiablePrescription) {
            Prescription immutablePrescription = ImmutablePrescription.copyOf(data);
            bundle.putString(Constants.DATA, JacksonUtils.toJson(immutablePrescription));
        } else {
            bundle.putString(Constants.DATA, JacksonUtils.toJson(data));
        }
        bundle.putBoolean(Constants.READ_ONLY, isReadOnly);
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new EditPrescriptionModel();
        data = JacksonUtils.fromJson(getArguments().getString(Constants.DATA), Prescription.class);
        isReadOnly = getArguments().getBoolean(Constants.READ_ONLY, false);
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
        List<SortedItem> sortedItems = model.parseData(getActivity(), data, isReadOnly);
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
            }
        });


        if (save != null) {
            Prescription data = PrescriptionHandler.fromHashMap(save);
            if (PrescriptionHandler.totalNumberPerFrequency(data) > 0D) {
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
            } else {
                Toast.makeText(getContext(), "请填写处方份量", Toast.LENGTH_SHORT).show();
                getBinding().recyclerView.scrollToPosition(5);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isReadOnly) {
            inflater.inflate(R.menu.menu_finish, menu);
        }
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

    @Subscribe
    public void onHideKeyboardEvent(HideKeyboardEvent event) {
        Systems.hideKeyboard(getContext());
    }
}
