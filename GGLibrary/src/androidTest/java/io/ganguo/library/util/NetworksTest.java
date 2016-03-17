package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 10/4/15.
 */
public class NetworksTest extends ApplicationTest {

    private Logger logger = LoggerFactory.getLogger(NetworksTest.class);

    public void testNetworkInfo() {
        logger.d("isConnected: " + Networks.isConnected(getContext()));
        logger.d("type: " + Networks.getType(getContext()));
        logger.d("isWifi: " + Networks.isWifi(getContext()));
        logger.d("isMobile: " + Networks.isMobile(getContext()));
        logger.d("mobileType: " + Networks.getMobileType(getContext()));
        logger.d("ip: " + Networks.getWifiIp(getContext()));
        logger.d("mac: " + Networks.getWifiMac(getContext()));
    }

}
