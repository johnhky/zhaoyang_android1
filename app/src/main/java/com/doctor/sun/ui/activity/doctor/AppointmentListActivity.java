package com.doctor.sun.ui.activity.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.doctor.auto.Factory;
import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.fragment.BaseFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;
import com.doctor.sun.vm.ItemSearch;
import com.google.common.base.Strings;


/**
 * Created by rick on 11/20/15.
 * 已预约患者
 */
@Factory(type = BaseFragment.class, id = "AppointmentListActivity")
public class AppointmentListActivity extends RefreshListFragment {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private ItemSearch searchItem;
    private String keyword;
    private boolean hasSearchItem = false;
    public static final String TAG = AppointmentListActivity.class.getSimpleName();

    @NonNull
    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_APPOINTMENT_LIST");
        getActivity().registerReceiver(receiver, filter);
    }

    public static Intent makeIntent(Context context) {
        Intent intent = SingleFragmentActivity.intentFor(context, "已预约患者", AppointmentListActivity.getArgs());
        return intent;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        AppContext.getInstance().setKeepState(null);
        PageCallback pageCallback = getPageCallback();
        api.searchAppointment(pageCallback.getPage(), keyword, "").enqueue(pageCallback);
    }


    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_appointment, R.layout.item_appointment);
        return adapter;
    }

    @Override
    protected void insertHeader() {
        super.insertHeader();
        insertSearchItem();
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何已预约患者";
    }

   /* @Override
    public int getMidTitle() {
        return R.string.title_appointment_list;
    }*/


    public void insertSearchItem() {
        if (searchItem == null) {
            hasSearchItem = true;
            searchItem = new ItemSearch();
            searchItem.setItemLayoutId(R.layout.item_search);
            searchItem.setItemId("id");
            searchItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    if (i == BR.keyword) {
                        if (Strings.isNullOrEmpty(searchItem.getKeyword())) {
                            if (Strings.isNullOrEmpty(keyword)) {
                                return;
                            }
                            keyword = searchItem.getKeyword();
                            getPageCallback().resetPage();
                            loadMore();
                        }
                    } else if (i == BR.submit) {
                        keyword = searchItem.getKeyword();
                        getPageCallback().resetPage();
                        loadMore();
                    }
                }
            });
        }
        if (getAdapter() != null) {
            getAdapter().add(searchItem);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_APPOINTMENT_LIST")) {
                createAdapter();
            }
        }
    };

}
