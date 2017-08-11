package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.event.HideKeyboardEvent;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by heky on 17/5/5.
 */

public class DiagnosisRecordList extends BaseItem {

    public ArrayList<ItemAutoInputRecord> datas = new ArrayList<>();
    private SimpleAdapter<ItemAutoInputRecord, ViewDataBinding> simpleAdapter;
    private String itemId;
    private RecyclerView.AdapterDataObserver observer;
    private Context context;

    public DiagnosisRecordList() {

    }

    public SimpleAdapter adapter(Context context) {
        this.context = context;
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

    public void addRecord() {
        if (datas != null) {
            int position = datas.size() + 1;
            final ItemAutoInputRecord autoInput = new ItemAutoInputRecord(R.layout.item_auto_record, "诊断 " + position + ":", "点击填写诊断");
            autoInput.setPosition(datas.size() + 1);
            autoInput.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (null != autoInput.mAdapter) {
                        autoInput.mAdapter.notifyDataSetChanged();
                    }
                }
            });
            autoInput.setListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (autoInput.list.size() > 0) {
                        String record = autoInput.list.get(position);
                        autoInput.setResult(record);
                        autoInput.dismissDialog();
                        autoInput.mAdapter.notifyDataSetChanged();
                    }
                }
            });
            if (datas.size() > 0) {
                if (TextUtils.isEmpty(datas.get(datas.size() - 1).getResult())) {
                    Toast.makeText(AppContext.me(), "请先完善上一条诊断信息", Toast.LENGTH_LONG).show();
                    return;
                }
                datas.add(autoInput);
                if (simpleAdapter != null) {
                    simpleAdapter.notifyItemInserted(datas.size() - 1);
                }
            } else {
                datas.add(autoInput);
                if (simpleAdapter != null) {
                    simpleAdapter.notifyItemInserted(datas.size() - 1);
                }
            }
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).dismissByUser = false;
            }
        }
    }

    public void addRecord(String autoInput, int position) {
        if (autoInput == null) {
            return;
        }
        if (datas != null) {
            final ItemAutoInputRecord object = new ItemAutoInputRecord(R.layout.item_auto_record, "诊断" + position + ":", "");
            object.setResult(autoInput);
            object.dismissByUser = true;
            object.setPosition(datas.size() + 1);
            object.setListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (object.list.size() > 0) {
                        String record = object.list.get(position);
                        object.setResult(record);
                        object.dismissDialog();
                        object.mAdapter.notifyDataSetChanged();
                    }
                }
            });
            datas.add(object);
            if (simpleAdapter != null) {
                simpleAdapter.notifyItemInserted(datas.size());
            }
        }
    }

    public void addRecords(List<String> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            addRecord(str, i + 1);
        }
    }

    @NonNull
    public ArrayList<String> getRecords() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < simpleAdapter.size(); i++) {
            ItemAutoInputRecord content = simpleAdapter.get(i);
            String input = content.getResult();
            if (!TextUtils.isEmpty(input)) {
                result.add(input);
            } else {
                Toast.makeText(AppContext.me(), "请完善诊断信息!", Toast.LENGTH_SHORT).show();
            }
        }
        return result;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (simpleAdapter.isEmpty()) {
            return null;
        } else {
            ArrayList<Object> arrayList = new ArrayList<>();
            for (int i = 0; i < simpleAdapter.size(); i++) {
                ItemAutoInputRecord autoInput = simpleAdapter.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("", autoInput.getResult());
                arrayList.add(hashMap);
            }
            HashMap<String, Object> result = new HashMap<>();
            result.put("diagnosis_record", arrayList);
            return result;
        }

    }
}
