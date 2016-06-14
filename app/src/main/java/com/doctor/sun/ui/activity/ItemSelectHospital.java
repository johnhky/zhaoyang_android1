package com.doctor.sun.ui.activity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.Area;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 4/6/2016.
 */
public class ItemSelectHospital extends BaseObservable implements LayoutId {
    public static final String TAG = ItemSelectHospital.class.getSimpleName();

    private ToolModule api = Api.of(ToolModule.class);

    private List<Area> lv1 = new ArrayList<>();
    private List<Area> lv2 = new ArrayList<>();
    private List<Area> lv3 = new ArrayList<>();
    private int lv1Position = 0;
    private int lv2Position = 0;
    private int lv3Position = 0;

    public ItemSelectHospital(final int lv1Id, final int lv2Id, final int lv3Id) {
        api.endemicAreaList().enqueue(new SimpleCallback<List<Area>>() {
            @Override
            protected void handleResponse(List<Area> response) {
                for (int i = 0; i < response.size(); i++) {
                    Area object = response.get(i);
                    lv1.add(object);
                    if (object.id == lv1Id) {
                        lv1Position = i;
                    }
                }
                List<Area> lv2Areas = lv2(lv1Position);
                for (int i = 0; i < lv2Areas.size(); i++) {
                    Area object = lv2Areas.get(i);
                    lv2.add(object);
                    if (object.id == lv2Id) {
                        lv2Position = i;
                    }
                }

                List<Area> lv3Areas = lv3(lv1Position,lv2Position);
                for (int i = 0; i < lv3Areas.size(); i++) {
                    Area object = lv3Areas.get(i);
                    lv3.add(object);
                    if (object.id == lv3Id) {
                        lv3Position = i;
                    }
                }

            }
        });
    }

    private List<Area> child(int parent, List<Area> data) {

        if (parent > data.size()) {
            return null;
        }
        if (parent < 0) {
            return null;
        }
        return data.get(parent).child;
    }

    public List<Area> lv2(int parent) {
        List<Area> child = child(parent, lv1);
        return child;
    }

    public List<Area> lv3(int lv1Spinner, int lv2Spinner) {
        List<Area> lv2 = lv2(lv1Spinner);

        return child(lv2Spinner, lv2);
    }

    public AdapterView.OnItemSelectedListener lv1Changed(final Spinner lv2Spinner, final Spinner lv3Spinner) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lv1Position == position) {
                    return;
                }
                lv1Position = position;
                setLv2(lv2(lv1Position));
                lv2Position = 0;
                lv3Position = 0;
                setLv3(lv3(lv1Position, lv2Position));
//                lv2Spinner.setSelection(0);
//                lv3Spinner.setSelection(0);
//                AbsSpinnerBindingAdapter.setEntries(lv2Spinner, lv2);
//                AbsSpinnerBindingAdapter.setEntries(lv3Spinner, lv3);
                notifyChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener lv2Changed(final Spinner lv3Spinner) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lv2Position == position) {
                    return;
                }
                lv2Position = position;
                lv3Position = 0;
//                lv3Spinner.setSelection(0);
                setLv3(lv3(lv1Position, lv2Position));
//                AbsSpinnerBindingAdapter.setEntries(lv3Spinner, lv3);
                notifyChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    public AdapterView.OnItemSelectedListener lv3Changed() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lv3Position = position;
                notifyPropertyChanged(BR.lv3Position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_hospital;
    }

    @Bindable
    public int getLv1Position() {
        return lv1Position;
    }

    public void setLv1Position(int lv1Position) {
        this.lv1Position = lv1Position;
        notifyPropertyChanged(BR.lv1Position);
    }

    @Bindable
    public int getLv2Position() {
        return lv2Position;
    }

    public void setLv2Position(int lv2Position) {
        this.lv2Position = lv2Position;
        notifyPropertyChanged(BR.lv2Position);
    }

    @Bindable
    public int getLv3Position() {
        return lv3Position;
    }

    public void setLv3Position(int lv3Position) {
        this.lv3Position = lv3Position;
        notifyPropertyChanged(BR.lv3Position);
    }

    @Bindable
    public List<Area> getLv1() {
        return lv1;
    }

    public void setLv1(List<Area> lv1) {
        this.lv1 = lv1;
        notifyPropertyChanged(BR.lv1);
    }

    @Bindable
    public List<Area> getLv2() {
        return lv2;
    }

    public void setLv2(List<Area> lv2) {
        this.lv2 = lv2;
        notifyPropertyChanged(BR.lv2);
    }

    @Bindable
    public List<Area> getLv3() {
        return lv3;
    }

    public void setLv3(List<Area> lv3) {
        this.lv3 = lv3;
        notifyPropertyChanged(BR.lv3);
    }

    public HashMap<String, Object> toJsonAnswer() {
        HashMap<String, Object> result = new HashMap<>();
        String[] type = new String[3];
        String[] content = new String[3];

        type[0] = String.valueOf(lv1.get(lv1Position).id);
        content[0] = lv1.get(lv1Position).name;

        type[1] = String.valueOf(lv2.get(lv2Position).id);
        content[1] = lv2.get(lv2Position).name;

        type[2] = String.valueOf(lv3.get(lv3Position).id);
        content[2] = lv3.get(lv3Position).name;

        result.put("type", type);
        result.put("content", content);


        return result;
    }
}
