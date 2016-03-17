package io.ganguo.library.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.OnNetworkEvent;
import io.ganguo.library.util.Networks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 网络改变 BroadcastReceiver
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private Logger logger = LoggerFactory.getLogger(NetworkChangeReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        // check status
        boolean isConnected = Networks.isConnected(context);
        Networks.Type type = Networks.getMobileType(context);

        // notify event
        EventHub.post(new OnNetworkEvent(isConnected, type));

        logger.i("isConnected: " + isConnected + " type:" + type);
    }

}
