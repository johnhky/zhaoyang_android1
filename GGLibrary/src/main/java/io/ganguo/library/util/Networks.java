package io.ganguo.library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

/**
 * 网络处理工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Networks {

    /**
     * 网络类型
     */
    public enum Type {
        _2G, _3G, _4G, WIFI, UNKNOWN
    }

    /**
     * 检测网络是连接
     *
     * @return isConnected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * Get network type
     *
     * @param context
     * @return ConnectivityManager.TYPE_WIFI | ConnectivityManager.TYPE_MOBILE
     */
    public static int getType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null || cm.getActiveNetworkInfo() == null) {
            return -1;
        }
        return cm.getActiveNetworkInfo().getType();
    }

    /**
     * is wifi network
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        return getType(context) == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * is mobile network
     *
     * @param context
     * @return
     */
    public static boolean isMobile(Context context) {
        return getType(context) == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * get mobile type
     *
     * @param context
     * @return
     */
    public static Type getMobileType(Context context) {
        // is wifi
        if (isWifi(context)) {
            return Type.WIFI;
        }
        // is mobile
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = tm.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return Type._2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return Type._3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return Type._4G;
            default:
                return Type.UNKNOWN;
        }
    }

    /**
     * get wifi mac
     *
     * @param context
     * @return
     */
    public static String getWifiMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info == null) {
            wifi.setWifiEnabled(true);
            // waiting enable
            SystemClock.sleep(2500);
            info = wifi.getConnectionInfo();
        }
        return info == null ? "Unknown" : info.getMacAddress();
    }

    /**
     * get wifi ip
     * # IPv4 192.168.1.1
     * # IPV6 not supported (formatIpAddress)
     *
     * @param context
     * @return
     */
    public static String getWifiIp(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wm.getDhcpInfo();
        if (dhcpInfo == null) {
            return null;
        }
        return Formatter.formatIpAddress(dhcpInfo.ipAddress);
    }

}