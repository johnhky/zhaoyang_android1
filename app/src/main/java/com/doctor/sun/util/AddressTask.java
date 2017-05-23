package com.doctor.sun.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by heky on 17/4/27.
 */

public class AddressTask extends AsyncTask<String, Void, ArrayList<Province>> {

    private Activity activity;
    private ProgressDialog progressDialog;
    private Callback callback;
    private String selectedProvince = "",selectedCity = "", selectedCounty="";
    private boolean hideProvince =false;
    private boolean hideCounty = false;

    public AddressTask(Activity activity){
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince){
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty){
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback){
        this.callback  =callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity,null,"正在初始化数据...",true,true);
    }

    @Override
    protected void onPostExecute(ArrayList<Province> provinces) {
        super.onPostExecute(provinces);
        progressDialog.dismiss();
        if (provinces.size()>0){
            AddressPicker picker = new AddressPicker(activity,provinces);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            if (hideCounty){
                picker.setColumnWeight(1/3.0,2/3.0);
            }else{
                picker.setColumnWeight(2/8.0,3/8.0,3/8.0);
            }
            picker.setSelectedItem(selectedProvince,selectedCity,selectedCounty);
            picker.setOnAddressPickListener(callback);
            picker.show();
        }else{
            callback.onAddressInitFailed();
        }
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params!=null){
            switch (params.length){
                case  1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince=params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince=params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
            }
        }
        ArrayList<Province>data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(activity.getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json,Province.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }


    public interface Callback extends AddressPicker.OnAddressPickListener {
        void onAddressInitFailed();
    }
}
