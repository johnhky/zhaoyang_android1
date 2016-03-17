package io.ganguo.library.service;

import android.app.Service;

import io.ganguo.library.core.event.EventHub;

/**
 * 后台服务 - 基类
 * <p/>
 * Created by zhihui_chen on 14-9-9.
 */
public abstract class BaseService extends Service  {

    @Override
    public void onCreate() {
        super.onCreate();

        EventHub.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventHub.unregister(this);
    }

}
