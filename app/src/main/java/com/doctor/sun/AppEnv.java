package com.doctor.sun;

import android.content.Context;
import android.util.Log;

import io.ganguo.library.Config;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.FileLogger;
import io.ganguo.library.util.log.LogConfig;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * App 环境配置
 * <p/>
 * Created by Tony on 3/4/15.
 */
public class AppEnv {

    private static Logger LOG = LoggerFactory.getLogger(AppEnv.class);

    /**
     * 开发环境配置信息
     * 根据app/build.gradle生成
     */
    public final static boolean DEV_MODE = BuildConfig.DEV_MODE;//是否处于测试环境
    public final static String BASE_URL = BuildConfig.BASE_URL;//server url
    public final static String MARKET_CHANNEL = BuildConfig.FLAVOR;//市场渠道

    /**
     * init
     */
    public static void init(Context context) {
        // App 数据保存目录
        Config.DATA_PATH = BuildConfig.DATA_PATH;
        // 日志配置
        LogConfig.LOGGER_CLASS = FileLogger.class;
        LogConfig.PRIORITY = DEV_MODE ? Log.VERBOSE : Log.INFO;
        LogConfig.FILE_PRIORITY = Log.ERROR;

        // app info
        LOG.i("****** AppEnvironment ******");
        LOG.i(" DEV_MODE: " + DEV_MODE);
        LOG.i(" APPLICATION_ID: " + BuildConfig.APPLICATION_ID);
        LOG.i(" VERSION_CODE: " + BuildConfig.VERSION_CODE);
        LOG.i(" VERSION_NAME: " + BuildConfig.VERSION_NAME);
        LOG.i(" ScreenSize: " + Systems.getScreenWidth(context) + "x" + Systems.getScreenHeight(context));
        LOG.i(" Category: " + Systems.getCategory(context));
        LOG.i(" FLAVOR: " + BuildConfig.FLAVOR);
        LOG.i(" BASE_URL: " + BASE_URL);
        LOG.i(" DATA_PATH: " + Config.DATA_PATH);
        LOG.i(" LOG_CONSOLE: " + LogConfig.PRIORITY);
        LOG.i(" LOG_FILE: " + LogConfig.FILE_PRIORITY);
        LOG.i("***************************");
    }

}
