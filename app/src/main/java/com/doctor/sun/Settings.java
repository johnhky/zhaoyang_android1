package com.doctor.sun;

import io.ganguo.library.Config;

/**
 * 设置选项
 * <p/>
 * Created by Tony on 10/24/15.
 */
public class Settings {

    private static final String SETTING_FIRST_OPEN = "setting_first_open";
    private static final String SETTING_LAST_VERSION = "setting_last_version";

    /**
     * 是否首次打开应用
     *
     * @return
     */
    public static boolean isFirstOpen() {
        return Config.getBoolean(SETTING_FIRST_OPEN, true);
    }

    /**
     * 设置是否首次打开
     *
     * @param isFirstOpen
     */
    public static void setFirstOpen(boolean isFirstOpen) {
        Config.putBoolean(SETTING_FIRST_OPEN, isFirstOpen);
    }

    /**
     * 获取上一次版本号
     *
     * @return
     */
    public static int getLastVersion() {
        return Config.getInt(SETTING_LAST_VERSION, BuildConfig.VERSION_CODE);
    }

    /**
     * 设置上一次版本号
     */
    public static void setLastVersion(int version) {
        Config.putInt(SETTING_LAST_VERSION, version);
    }

}
