//package com.doctor.sun.ui.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.util.Log;
//import android.view.View;
//
//import com.doctor.sun.R;
//import com.doctor.sun.bean.Constants;
//import com.doctor.sun.entity.Appointment;
//import com.doctor.sun.http.Api;
//import com.doctor.sun.http.callback.PageCallback;
//import com.doctor.sun.module.AppointmentModule;
//import com.doctor.sun.module.AuthModule;
//import com.doctor.sun.ui.adapter.ConsultedAdapter;
//import com.doctor.sun.ui.adapter.SimpleAdapter;
//
//import io.ganguo.library.Config;
//import io.realm.RealmChangeListener;
//
///**
// * Created by rick on 12/18/15.
// */
//@Deprecated
//public class ConsultedFragment extends RefreshListFragment {
//    public static final String TAG = ConsultedFragment.class.getSimpleName();
//    public static final String STARTTIME = "STARTIME";
//
//    private AppointmentModule api = Api.of(AppointmentModule.class);
//    private RealmChangeListener listener;
//    private PageCallback<Appointment> callback;
//
//    public ConsultedFragment() {
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        listener = new RealmChangeListener() {
//            /**
//             * Called when a transaction is committed.
//             *
//             * @param element
//             */
//            @Override
//            public void onChange(Object element) {
//                getAdapter().notifyDataSetChanged();
//            }
//        };
//        realm.addChangeListener(listener);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        initListener();
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Config.putLong(STARTTIME, System.currentTimeMillis());
//        Log.e(TAG, "startTime: " + Config.getLong(STARTTIME, -1));
//    }
//
//    @Override
//    public void onDestroy() {
//        if (!realm.isClosed()) {
//            if (listener != null) {
//                realm.removeChangeListener(listener);
//            }
//        }
//        super.onDestroy();
//    }
//
//    @NonNull
//    @Override
//    public SimpleAdapter createAdapter() {
//        ConsultedAdapter adapter = new ConsultedAdapter(getContext(), realm, Config.getLong(STARTTIME, -1));
//        int userType = Config.getInt(Constants.USER_TYPE, -1);
//        if (userType == AuthModule.PATIENT_TYPE) {
//            adapter.mapLayout(R.layout.item_appointment, R.layout.p_item_consulted);
//        } else {
//            adapter.mapLayout(R.layout.item_appointment, R.layout.item_consulted);
//        }
//        return adapter;
//    }
//
//    @Override
//    protected void loadMore() {
//        super.loadMore();
//        callback = new PageCallback<Appointment>(getAdapter()) {
//            @Override
//            public void onFinishRefresh() {
//                super.onFinishRefresh();
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        };
//        int userType = Config.getInt(Constants.USER_TYPE, -1);
//        if (userType == AuthModule.PATIENT_TYPE) {
//            api.pFinishList(callback.getPage()).enqueue(callback);
//        } else {
//            api.dFinishList(callback.getPage()).enqueue(callback);
//        }
//    }
//
//    private void initListener() {
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                binding.swipeRefresh.setRefreshing(true);
//                callback.setRefresh();
//                loadMore();
//            }
//        });
//    }
//}
