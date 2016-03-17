package com.doctor.sun.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限检查工具
 * Created by Lynn on 2/16/16.
 */
public class PermissionsUtil {
    public static final int PERMISSION_REQUEST_CODE = 0;
    //具体权限
    public static final String PERMISSION_WES = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_RES = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_AFL = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACL = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_CALL = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    private final Context mActivity;
    private GrantedPermissionActionListener grantedPermissionActionListener;

    public PermissionsUtil(Activity context) {
        mActivity = context;
    }

    public PermissionsUtil(Activity context, GrantedPermissionActionListener grantedPermissionActionListener) {
        this(context);
        this.grantedPermissionActionListener = grantedPermissionActionListener;
    }

    /**
     * 检查并且申请权限
     *
     * @param activity
     * @param permissions
     */
    public void checkPermissionAndRequest(Activity activity, String... permissions) {
        checkPermissionAndRequest(activity, PermissionsUtil.PERMISSION_REQUEST_CODE, permissions);
    }

    /**
     * 检查并且申请权限, 使用自定义REQUEST_CODE
     *
     * @param activity
     * @param permissions
     */
    public void checkPermissionAndRequest(final Activity activity, final int requestCode, final String... permissions) {
        if (lacksPermissions(permissions)) {
            //权限缺失
            requestPermissions(activity, requestCode, permissions);
        }
    }

    /**
     * 申请权限, 自定义REQUEST_Code
     *
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions,
                requestCode);
    }

    /**
     * 判断权限集合
     *
     * @param permissions
     * @return
     */
    public boolean lacksPermissions(final String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }

        if (grantedPermissionActionListener != null) {
            grantedPermissionActionListener.grantedAction();
        }

        return false;
    }

    /**
     * 判断单个权限
     *
     * @param permission
     * @return
     */
    public boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    public static void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public interface GrantedPermissionActionListener {
        void grantedAction();
    }
}
