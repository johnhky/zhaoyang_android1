package io.ganguo.library.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.facebook.device.yearclass.YearClass;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import io.ganguo.library.Config;
import io.ganguo.library.bean.Globals;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 系统/app 工具
 * <p/>
 * Created by Tony on 9/30/15.
 */
public class Systems {
    private static final Logger LOG = LoggerFactory.getLogger(Systems.class);

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * dp to px
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        return (int) applyDimension(context, TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    /**
     * 单位转换
     *
     * @param context
     * @param unit    TypedValue.COMPLEX_UNIT_DIP
     * @param value   px
     * @return
     */
    public static float applyDimension(Context context, int unit, float value) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit, value, displayMetrics);
    }

    /**
     * 打开软键盘
     *
     * @param window
     */
    public static void showKeyboard(Window window, View view) {
        if (window.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) window.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (view != null) {
                inputManager.showSoftInput(view, 0);
            }
            inputManager.showSoftInputFromInputMethod(window.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            LOG.e("Sigh, cant even hide keyboard ", e);
        }
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("程序包名无法找到", e);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取当前程序版本名称
     */
    public static String getVersionName(Context context) {
        return getVersionName(context, context.getPackageName());
    }

    /**
     * 获取程序版本名称
     */
    public static String getVersionName(Context context, String packageName) {
        String versionName;
        try {
            // Get the package info
            PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = pi.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("获取程序版本名称", e);
            return "";
        }
        return versionName;
    }

    /**
     * 获取当前代码版本号
     */
    public static int getVersionCode(Context context) {
        int localVersion = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            localVersion = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            LOG.w("获取当前代码版本号", e);
        }
        return localVersion;
    }

    /**
     * 设备级别
     */
    public enum Category {
        LOW, MEDIUM, HIGH
    }

    /**
     * 设备级别
     *
     * @return
     */
    public static Category getCategory(Context context) {
        int year = YearClass.get(context);
        if (year >= 2014) {
            return Category.HIGH;
        } else if (year >= 2012) {
            return Category.MEDIUM;
        } else {
            return Category.LOW;
        }
    }

    /**
     * 获取设备唯一码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (Strings.isEmpty(deviceId)) {
            deviceId = Config.getString(Globals.KEY_DEVICE_ID);
        }
        if (Strings.isEmpty(deviceId)) {
            deviceId = Strings.randomUUID();
            Config.putString(Globals.KEY_DEVICE_ID, deviceId);
        }
        return deviceId;
    }

    /**
     * 设置状态栏和导航栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setBarTranslucent(Activity activity) {
        setStatusTranslucent(activity);
        setNavigationTranslucent(activity);
    }

    /**
     * 设置导航栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setNavigationTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明导航栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setNavigationColor(activity, Color.TRANSPARENT);
    }

    /**
     * 设置状态栏透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void setStatusTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setStatusColor(activity, Color.TRANSPARENT);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(19)
    public static void setBarColor(Activity activity, int color) {
        if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;

        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        window.setAttributes(winParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(color);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(21)
    public static void setStatusColor(Activity activity, int color) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);
    }

    /**
     * 设置状态栏和导航栏的颜色
     *
     * @param activity
     * @param color
     */
    @TargetApi(21)
    public static void setNavigationColor(Activity activity, int color) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        winParams.flags |= bits;
        window.setAttributes(winParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(color);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(color);
    }


}
