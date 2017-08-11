package com.doctor.sun.ui.activity.doctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.fragment.BaseFragment;
import com.doctor.sun.ui.fragment.RefreshListFragment;
import com.doctor.sun.ui.fragment.SortedListFragment;
import com.doctor.sun.vm.ItemSearch;
import com.google.common.base.Strings;

/**
 * Created by rick on 1/6/2016.
 * 随访列表
 */
@Factory(type = BaseFragment.class, id = "AfterServiceActivity")
public class AfterServiceActivity extends RefreshListFragment {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private ItemSearch searchItem;
    private String keyword;
    private boolean hasSearchItem = false;
    public static final String TAG = AfterServiceActivity.class.getSimpleName();
    @NonNull
    public static Bundle getArgs(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME,TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_APPOINTMENT_LIST");
        getActivity().registerReceiver(receiver,filter);
        setHasOptionsMenu(true);
    }

    public static Intent makeIntent(Context context){
        Intent intent = SingleFragmentActivity.intentFor(context,"随访列表",AfterServiceActivity.getArgs());
        return intent;
    }
    @Override
    protected void loadMore() {
        super.loadMore();
        api.doctorOrders("", keyword, getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @Override
    protected void insertHeader() {
        super.insertHeader();
        insertSearchItem();
    }

    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(getContext(), ContactActivity.DOCTORS_CONTACT);
        startActivity(intent);
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何随访任务";
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_patient_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_patients: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void insertSearchItem() {
        if (searchItem==null){
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
        if (getAdapter()!=null){
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
            if (intent.getAction().equals("UPDATE_APPOINTMENT_LIST")){
                loadMore();
            }
        }
    };
}
