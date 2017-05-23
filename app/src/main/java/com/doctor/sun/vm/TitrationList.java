package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.immutables.Titration;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.util.JacksonUtils;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heky on 17/4/24.
 */

public class TitrationList extends ItemTextInput2 {

    private ArrayList<TitrationTextInput> datas = new ArrayList<>();
    private SimpleAdapter<TitrationTextInput, ViewDataBinding> simpleAdapter;
    private RecyclerView.AdapterDataObserver observer;

    public TitrationList(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
        setHint(hint);
    }

    public SimpleAdapter adapter(Context context) {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter<>();
            simpleAdapter.setData(datas);
            simpleAdapter.onFinishLoadMore(true);
            if (observer != null) {
                simpleAdapter.registerAdapterDataObserver(observer);
            }
        }
        return simpleAdapter;
    }

    public void addTitration() {
        if (Integer.valueOf(getHint())!=-1){
            if (datas != null) {
                TitrationTextInput object = new TitrationTextInput(R.layout.item_titration_tab, getSubTitle(), "");
                object.setEnabled(enabled);
                object.setPosition(datas.size() + 1);
                if (datas.size() == 0) {
                    object.setResult("1");
                }
                datas.add(object);
                if (simpleAdapter != null) {
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        }else{
            Toast.makeText(AppContext.me(),"请先选择药品单位!",Toast.LENGTH_LONG).show();
        }

    }

    public void addTitration(Titration titration) {
        if (titration == null) {
            return;
        }
        if (datas != null) {
            TitrationTextInput object = new TitrationTextInput(R.layout.item_titration_tab, getSubTitle(), "");
            object.setPosition(datas.size() + 1);
            if (!Strings.isNullOrEmpty(titration.take_medicine_days)) {
                object.setResult(titration.take_medicine_days);
            }
            if (!Strings.isNullOrEmpty(titration.morning)) {
                object.setMorning(titration.morning);
            }
            if (!Strings.isNullOrEmpty(titration.noon)) {
                object.setNoon(titration.noon);
            }
            if (!Strings.isNullOrEmpty(titration.night)) {
                object.setNight(titration.night);
            }
            if (!Strings.isNullOrEmpty(titration.before_sleep)) {
                object.setBefore_sleep(titration.before_sleep);
            }
            datas.add(object);
            if (simpleAdapter != null) {
                simpleAdapter.notifyItemInserted(datas.size());
            }
        }
    }

    public void addTitrations(List<Titration> titrationList) {
        if (titrationList == null) {
            return;
        }
        for (Titration titration : titrationList) {
            addTitration(titration);
        }
    }

    @Override
    public String getValue() {
        ArrayList<Titration> titrations = getTitrations();
        if (titrations.size()>0){
            Gson gson = new GsonBuilder().create();
            String str = gson.toJson(titrations);
            return str;
        }else{
            return "";
        }
    }

    @NonNull
    public ArrayList<Titration> getTitrations() {
        ArrayList<Titration> content = new ArrayList<>();
        for (int i = 0; i < simpleAdapter.size(); i++) {
            TitrationTextInput textInput = simpleAdapter.get(i);
            if (!TextUtils.isEmpty(textInput.getResult())) {
                Titration titration = new Titration();
                titration.setBefore_sleep(textInput.getBefore_sleep());
                titration.setTake_medicine_days(textInput.getResult());
                titration.setMorning(textInput.getMorning());
                titration.setNight(textInput.getNight());
                titration.setNoon(textInput.getNoon());
                content.add(titration);
            } else {
                Toast.makeText(AppContext.me(), "用药天数不能为空!", Toast.LENGTH_LONG).show();
            }

        }
        return content;
    }

    public SimpleAdapter<TitrationTextInput, ViewDataBinding> getSimpleAdapter() {
        return simpleAdapter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_titrationlist;
    }
}
