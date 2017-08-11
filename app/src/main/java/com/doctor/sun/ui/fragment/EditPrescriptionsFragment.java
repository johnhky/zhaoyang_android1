package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.doctor.auto.Factory;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.event.HideKeyboardEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.Titration;
import com.doctor.sun.model.EditPrescriptionModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vm.BaseItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.Systems;
import io.realm.Realm;

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
        Gson gson = new GsonBuilder().create();
        bundle.putString(Constants.DATA, gson.toJson(data));
        bundle.putBoolean(Constants.READ_ONLY, isReadOnly);
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new EditPrescriptionModel();
        Gson gson = new GsonBuilder().create();
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
            if (PrescriptionHandler.totalNumberPerFrequency(data) > 0D || data.getTitration().size() > 0) {
                if (data.getTitration().size() > 0) {
                    if (data.getTitration().size() == 1) {
                        Titration titration = data.getTitration().get(data.getTitration().size()-1);
                        if (PrescriptionHandler.totalTiNumberPerFrequency(titration)<=0D){
                            Toast.makeText(AppContext.me(), "用药剂量不能为空!", Toast.LENGTH_SHORT).show();
                            return;
                        }
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
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA, jsonData);
                        intent.putExtras(bundle);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    } else {
                        if (Integer.parseInt(data.getTitration().get(data.getTitration().size() - 1).getTake_medicine_days()) <= Integer.parseInt(data.getTitration().get(data.getTitration().size() - 2).getTake_medicine_days())) {
                            Toast.makeText(getActivity(), "用药天数不能小于上一用药天数!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Titration titration = data.getTitration().get(data.getTitration().size()-1);
                        if (PrescriptionHandler.totalTiNumberPerFrequency(titration)<=0D){
                            Toast.makeText(AppContext.me(), "用药剂量不能为空!", Toast.LENGTH_SHORT).show();
                            return;
                        }
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
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.DATA, jsonData);
                        intent.putExtras(bundle);
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                } else {
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
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.DATA, jsonData);
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
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
