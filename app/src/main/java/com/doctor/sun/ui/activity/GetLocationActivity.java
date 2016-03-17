package com.doctor.sun.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.tool.util.L;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.util.PermissionsUtil;

import java.util.Iterator;

import io.ganguo.library.common.ToastHelper;

/**
 * 获取定位
 * Created by rick on 6/1/2016.
 */
public abstract class GetLocationActivity extends BaseActivity2 {
    private static final int LOCATION_REQUEST_CODE = 10;
    private static final int PRE_LOCATION_REQUEST_CODE = 11;
    public LocationManager lm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已成功授权
                    getLocation();
                } else {
                    //失败
                    ToastHelper.showMessage(this, "授权失败, 请手动选择地址");
                }
                break;
            case PRE_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //已成功授权

                } else {
                    //失败
                    ToastHelper.showMessage(this, "授权失败");
                }
                break;
        }
    }

    /**
     * 对于6.0后的系统，该方法调用的时机只在获取权限后
     */
    protected void getLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        PermissionsUtil permissionsUtil = new PermissionsUtil(this, new PermissionsUtil.GrantedPermissionActionListener() {
            @Override
            public void grantedAction() {
                //获取位置信息
                //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
                try {
                    //为获取地理位置信息时设置查询条件
                    final String bestProvider = lm.getBestProvider(getCriteria(), true);
                    Location location = lm.getLastKnownLocation(bestProvider);
                    Log.d("grantedAction", "clicked");
                    //        Location location= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.d("location: ", location + "");
                    updateLocation(location);
                    //监听状态
                    lm.addGpsStatusListener(listener);
                    //绑定监听，有4个参数
                    //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
                    //参数2，位置信息更新周期，单位毫秒
                    //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
                    //参数4，监听
                    //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

                    // 1秒更新一次，或最小位移变化超过1米更新一次；
                    //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                    //        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        permissionsUtil.checkPermissionAndRequest(this, LOCATION_REQUEST_CODE,
                PermissionsUtil.PERMISSION_ACL, PermissionsUtil.PERMISSION_AFL);
    }

    //位置监听
    private LocationListener locationListener = new LocationListener() {

        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            updateLocation(location);
            Log.i(TAG, "时间：" + location.getTime());
            Log.i(TAG, "经度：" + location.getLongitude());
            Log.i(TAG, "纬度：" + location.getLatitude());
            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        /**
         * GPS状态变化时触发
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(final String provider) {
            PermissionsUtil permissionsUtil = new PermissionsUtil(GetLocationActivity.this, new PermissionsUtil.GrantedPermissionActionListener() {
                @Override
                public void grantedAction() {
                    try {
                        Location location = lm.getLastKnownLocation(provider);
                        updateLocation(location);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });

            permissionsUtil.checkPermissionAndRequest(GetLocationActivity.this, PRE_LOCATION_REQUEST_CODE,
                    PermissionsUtil.PERMISSION_ACL, PermissionsUtil.PERMISSION_AFL);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            updateLocation(null);
        }
    };

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "卫星状态改变");
                    //获取当前状态
                    GpsStatus gpsStatus = lm.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    Log.e(TAG, "搜索到：" + count + "颗卫星");
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        }

        ;
    };

    /**
     * 实时更新文本内容
     *
     * @param location
     */
    protected abstract void updateLocation(Location location);

    /**
     * 返回查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        onLocationGot();
    }

    protected void onLocationGot() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (lm != null) {
            lm.removeGpsStatusListener(listener);
            lm.removeUpdates(locationListener);
        }
    }

}
