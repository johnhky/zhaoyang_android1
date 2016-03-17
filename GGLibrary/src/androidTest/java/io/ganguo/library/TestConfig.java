package io.ganguo.library;

import io.ganguo.library.util.Files;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 10/4/15.
 *
 * /data/data/io.ganguo.library.test
 *
 */
public class TestConfig extends ApplicationTest {

    private Logger logger = LoggerFactory.getLogger(TestConfig.class);

    public void testConfigInfo() {
        logger.d(Config.getDataPath());
        logger.d(Config.getImagePath());
        logger.d(Config.getImageCachePath());

        logger.d("AppPath: " + Config.getDataPath());
        logger.d("DateSize: " + Config.getDataSize());
        logger.d("SDCardMounted: " + Files.isSdCardMounted());
        logger.d("AvailableSize: " + Files.getSDCardAvailableSize());
    }

}
