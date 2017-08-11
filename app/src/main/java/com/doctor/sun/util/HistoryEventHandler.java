package com.doctor.sun.util;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.widget.AppointmentHistoryDialog;
import com.doctor.sun.ui.widget.HistoryListDialog;
import com.squareup.otto.Subscribe;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16-10-19.
 */

public class HistoryEventHandler {

    private FragmentManager fragmentManager;

    public HistoryEventHandler(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static HistoryEventHandler register(FragmentManager fragmentManager) {
        HistoryEventHandler historyEventHandler = new HistoryEventHandler(fragmentManager);
        EventHub.register(historyEventHandler);
        return historyEventHandler;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public static void unregister(HistoryEventHandler handler) {
        handler.setFragmentManager(null);
        EventHub.unregister(handler);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Subscribe
    public void onHistoryEvent(AppointmentHistoryEvent event) {
        Appointment data = event.getData();
        if (event.isHistoryList()||!HistoryListDialog.isShowBefore(data.getRecord().getMedicalRecordId())){
            HistoryListDialog.newInstance(data.getRecord().getMedicalRecordId()).show(fragmentManager, HistoryListDialog.TAG);
        }else{
            AppointmentHistoryDialog.newInstance(data).show(fragmentManager, AppointmentHistoryDialog.TAG);
        }

     /*
        if (event.isHistoryList() || !HistoryListDialog.isShowBefore(event.getData().getRecord().getMedicalRecordId())) {
            HistoryListDialog.newInstance(data.getRecord().getMedicalRecordId()).show(fragmentManager, HistoryListDialog.TAG);
        } else if (index >= 0) {
            AppointmentHistoryDialog.newInstance(data).show(fragmentManager, AppointmentHistoryDialog.TAG);
        }*/
    }
}
