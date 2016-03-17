package io.ganguo.library.core.event;

import io.ganguo.library.util.Networks;

/**
 * 网络事件
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class OnNetworkEvent implements Event {

    private boolean isConnected;
    private Networks.Type type;

    public OnNetworkEvent(boolean isConnected, Networks.Type type) {
        this.isConnected = isConnected;
        this.type = type;
    }

    public boolean isWifi() {
        return getType() == Networks.Type.WIFI;
    }

    public boolean isMobile() {
        return !isWifi();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Networks.Type getType() {
        return type;
    }

    public void setType(Networks.Type type) {
        this.type = type;
    }
}
