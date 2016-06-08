package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityContactBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.ContactAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;
import com.doctor.sun.ui.binding.CustomBinding;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.NameComparator;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rick on 11/25/15.
 */
public class ContactActivity extends BaseActivity2 {
    public static final int DOCTORS_CONTACT = 33;
    public static final int PATIENTS_CONTACT = 22;

    private DiagnosisModule diagnosisModule = Api.of(DiagnosisModule.class);
    private ImModule imModule = Api.of(ImModule.class);
    private ActivityContactBinding binding;
    private ContactAdapter mAdapter;
    private PageCallback callback;
    private ArrayList<LayoutId> allData;

    public static Intent makeIntent(Context context, int code, @LayoutRes int layoutId) {
        Intent i = new Intent(context, ContactActivity.class);
        i.putExtra(Constants.REQUEST_CODE, code);
        i.putExtra(Constants.LAYOUT_ID, layoutId);
        return i;
    }

    public static Intent makeIntent(Context context, int code) {
        Intent i = new Intent(context, ContactActivity.class);
        i.putExtra(Constants.REQUEST_CODE, code);
        return i;
    }


    private int getRequestCode() {
        return getIntent().getIntExtra(Constants.REQUEST_CODE, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact);

        binding.setHeader(getHeaderViewModel());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//      getResources().getDrawable(R.drawable.shape_divider)));

        mAdapter = (ContactAdapter) createAdapter();
        int intExtra = getIntent().getIntExtra(Constants.LAYOUT_ID, -1);
        if (intExtra != -1) {
            mAdapter.mapLayout(R.layout.item_contact, intExtra);
        }
        callback = createCallback();
        binding.recyclerView.setAdapter(mAdapter);

        binding.fastScroller.setListView(binding.recyclerView);
        mAdapter.onFinishLoadMore(true);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (getRequestCode() == Constants.DOCTOR_REQUEST_CODE) {
                    String key = s.toString();
                    if (key.equals("")) {
                        CustomBinding.drawableLeft(binding.etSearch, R.drawable.ic_search_clicked);
                    } else {
                        CustomBinding.drawableLeft(binding.etSearch, R.drawable.bg_transparent);
                    }
                    getCallback().resetPage();
                    getAdapter().onFinishLoadMore(false);
                    getAdapter().clear();
                    diagnosisModule.searchDoctor(getCallback().getPage(), key).enqueue(getCallback());
                } else {
                    final String key = s.toString();
                    if (key.equals("")) {
                        if (allData != null) {
                            mAdapter.clear();
                            mAdapter.addAll(allData);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        allData = new ArrayList<LayoutId>(mAdapter.getData().size());
                        allData.addAll(mAdapter);
                        Collection<LayoutId> filter = Collections2.filter(allData, new Predicate<LayoutId>() {
                            @Override
                            public boolean apply(LayoutId input) {
                                NameComparator.Name name = (NameComparator.Name) input;
                                String name1 = name.getName();
                                return name1.contains(key);
                            }
                        });
                        mAdapter.clear();
                        mAdapter.addAll(filter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        loadMore();
    }

    @NonNull
    private PageCallback createCallback() {
        return new PageCallback(mAdapter) {
            @Override
            protected void handleResponse(PageDTO response) {
                super.handleResponse(response);
                Collections.sort(getAdapter(), new NameComparator());
                mAdapter.updatePosition();
            }
        };
    }

    protected void getContactList() {
        imModule.doctorContactList().enqueue(new ApiCallback<List<Contact>>() {
            @Override
            protected void handleResponse(List<Contact> response) {
                getAdapter().clear();
                getAdapter().addAll(response);
                Collections.sort(getAdapter(), new NameComparator());
                mAdapter.updatePosition();
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    protected void getPContactList() {
        imModule.pContactList().enqueue(new ApiCallback<List<Contact>>() {
            @Override
            protected void handleResponse(List<Contact> response) {
                getAdapter().clear();
                getAdapter().addAll(response);
                Collections.sort(getAdapter(), new NameComparator());
                mAdapter.updatePosition();
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    protected void loadMore() {
        switch (getRequestCode()) {
            case Constants.DOCTOR_REQUEST_CODE: {
                break;
            }
            case PATIENTS_CONTACT: {
                getPContactList();
                break;
            }
            case DOCTORS_CONTACT: {
                getContactList();
                break;
            }
            default: {
                getPContactList();
                break;
            }
        }
    }

    protected LoadMoreAdapter getAdapter() {
        return mAdapter;
    }

    protected ContactAdapter getContactAdapter() {
        return mAdapter;
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("通讯录");
    }


    @NonNull
    protected LoadMoreAdapter createAdapter() {
        return new ContactAdapter(this);
    }


    public PageCallback getCallback() {
        return callback;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.fastScroller.removeDis();
    }
}
