package com.doctor.sun.vm;

import android.view.View;
import android.widget.Toast;

import com.alipay.tscenter.biz.rpc.vkeydfp.result.BaseResult;
import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heky on 17/4/17.
 */

public class TitrationTextInput extends ItemTextInput2 {

    public String morning = "",noon = "",night = "",before_sleep="";
    boolean isChecked;

    public TitrationTextInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getBefore_sleep() {
        return before_sleep;
    }

    public void setBefore_sleep(String before_sleep) {
        this.before_sleep = before_sleep;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String getError() {
        return "请补全以上内容";
    }

    public void removeThis(SimpleAdapter simpleAdapter){
        if (!isEnabled()){
            return;
        }
        if (simpleAdapter.size()<=1){
            Toast.makeText(AppContext.me(),"最少保留一个用法用量!",Toast.LENGTH_LONG).show();
        }else{
            notifyPropertyChanged(BR.removed);
            simpleAdapter.removeItem(this);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        HashMap<String,Object>result = new HashMap<>();
        ArrayList<HashMap<String,Object>>hashMaps = new ArrayList<>();
        HashMap<String,Object>objectHashMap = new HashMap<>();
        objectHashMap.put("morning",getMorning());
        objectHashMap.put("take_medicine_days",getResult());
        objectHashMap.put("night",getNight());
        objectHashMap.put("before_sleep",getBefore_sleep());
        objectHashMap.put("noon",getNoon());
        hashMaps.add(objectHashMap);
        result.put("titration",hashMaps);
        return result;
    }
}
