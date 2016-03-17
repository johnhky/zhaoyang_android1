package io.ganguo.library.core.event;

import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import io.ganguo.library.util.Tasks;

/**
 * 事件中心
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class EventHub {

    /**
     * 事件总线
     */
    private static final Bus BUS = new Bus(ThreadEnforcer.MAIN);

    /**
     * 注册事件源
     *
     * @param o
     */
    public static void register(Object o) {
        BUS.register(o);
    }

    /**
     * 释放事件源
     *
     * @param o
     */
    public static void unregister(Object o) {
        BUS.unregister(o);
    }

    /**
     * 发送事件
     *
     * @param event
     */
    public static void post(final Event event) {
        // 只能通过主线路进行post
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e("", "post: ");
            BUS.post(event);
        } else {
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "post: ");
                    BUS.post(event);
                }
            });
        }
    }

}
