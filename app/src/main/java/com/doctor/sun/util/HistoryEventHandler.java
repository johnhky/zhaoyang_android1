package com.doctor.sun.util;

import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.event.AppointmentHistoryEvent;
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

    public static void ungister(HistoryEventHandler handler) {
        EventHub.unregister(handler);
    }

    @Subscribe
    public void onHistoryEvent(AppointmentHistoryEvent event) {
        // TODO: 替换为历史记录入口
        Toast.makeText(AppContext.me(), "历史记录", Toast.LENGTH_SHORT).show();
    }
}
