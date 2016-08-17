//package com.doctor.sun.ui.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//
//import com.doctor.sun.http.Api;
//import com.doctor.sun.module.AppointmentModule;
//
///**
// * Created by rick on 30/12/2015.
// */
//public class PUrgentCallFragment extends RefreshListFragment {
//    private AppointmentModule api = Api.of(AppointmentModule.class);
//
//
//    public static PUrgentCallFragment getInstance() {
//        PUrgentCallFragment     instance = new PUrgentCallFragment();
//        return instance;
//    }
//
//    public PUrgentCallFragment() {
//    }
//
//    @Override
//    protected void loadMore() {
//        super.loadMore();
//        api.pUrgentCalls(getPageCallback().getPage()).enqueue(getPageCallback());
//    }
//
//}
