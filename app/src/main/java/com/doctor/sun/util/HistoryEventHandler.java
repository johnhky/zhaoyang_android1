package com.doctor.sun.util;

import android.support.v4.app.FragmentManager;

import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.ui.widget.AppointmentHistoryDialog;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by kb on 16-10-19.
 */

public class HistoryEventHandler {

    public static HistoryEventHandler register() {
        HistoryEventHandler historyEventHandler = new HistoryEventHandler();
        EventHub.register(historyEventHandler);
        return historyEventHandler;
    }

    public static void unregister(HistoryEventHandler handler) {
        EventHub.unregister(handler);
    }

    @Subscribe
    public void onHistoryEvent(AppointmentHistoryEvent event) {
        Appointment data = event.getData();
        FragmentManager fragmentManager = event.getFragmentManager();
        AppointmentHistoryDialog.newInstance(data).show(fragmentManager, "AppointmentHistory");
    }
}
