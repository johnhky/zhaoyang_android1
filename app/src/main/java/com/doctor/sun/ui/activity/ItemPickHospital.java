package com.doctor.sun.ui.activity;

import android.util.SparseArray;

import com.doctor.sun.R;
import com.doctor.sun.entity.Area;
import com.doctor.sun.entity.OptionsPair;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 4/6/2016.
 */
public class ItemPickHospital extends BaseItem {
    public static final String TAG = ItemPickHospital.class.getSimpleName();
    private final String[] areaNames;
    private boolean isInit = false;
    private int lv1Id;
    private int lv2Id;
    private int lv3Id;
    private final String path;

    private ToolModule api = Api.of(ToolModule.class);

    private List<Area> lv1Data;
    private SparseArray<Area> hashMap = new SparseArray<Area>();
    private int lv3Position = 0;
    private int lv2Position = 0;
    private int lv1Position = 0;

    public ItemPickHospital(String[] lastAnswerContent, String path, final int lv1Id, final int lv2Id, final int lv3Id) {
        this.areaNames = lastAnswerContent;

        this.path = path;
        this.lv1Id = lv1Id;
        this.lv2Id = lv2Id;
        this.lv3Id = lv3Id;
    }

    public void initData() {
        if (path.equals("") || isInit) {
            return;
        }

        api.endemicAreaList(path).enqueue(new SimpleCallback<List<Area>>() {
            @Override
            protected void handleResponse(List<Area> response) {
                lv1Data = response;
                parseData(response, 0);
                isInit = true;
                notifyChange();
            }
        });
    }

    public void parseData(List<Area> data, int position) {
        if (data == null) {
            return;
        }
        if (data.size() <= position) {
            return;
        }

        Area value = data.get(position);

        if (lv1Id == value.id) {
            lv1Position = position;
        } else if (lv2Id == value.id) {
            lv2Position = position;

        } else if (lv3Id == value.id) {
            lv3Position = position;
        }

        hashMap.put(value.id, value);
        parseData(data, position + 1);
        parseData(value.child, 0);
    }

    public List<Area> lv2() {
        Area area = hashMap.get(lv1Id);
        if (area == null) {
            return null;
        }
        return area.child;
    }

    public List<Area> lv3() {
        Area area = hashMap.get(lv2Id);
        if (area == null) {
            return null;
        }
        return area.child;
    }

    public List<Area> lv1() {
        initData();
        return lv1Data;
    }

    //TODO use recursive
    public void onLv1Selected(int position) {
        Area lv1Area = lv1Data.get(position);
        int newLv1Id = lv1Area.id;
        if (lv1Id == newLv1Id) {
            return;
        }
        areaNames[0] = lv1Area.name;
        lv1Id = newLv1Id;

        Area lv2Area = lv2().get(0);
        lv2Id = lv2Area.id;
        areaNames[1] = lv2Area.name;

        Area lv3Area = lv3().get(0);
        lv3Id = lv3Area.id;
        areaNames[2] = lv3Area.name;


        lv1Position = position;
        lv2Position = 0;
        lv3Position = 0;
        notifyChange();
    }


    public void onLv2Selected(int position) {
        Area lv2Area = lv2().get(position);
        int newLv2Id = lv2Area.id;
        if (lv2Id == newLv2Id) {
            return;
        }

        areaNames[1] = lv2Area.name;
        lv2Id = newLv2Id;

        Area lv3Area = lv3().get(0);
        lv3Id = lv3Area.id;
        areaNames[2] = lv3Area.name;

        lv2Position = position;
        lv3Position = 0;
        notifyChange();
    }

    public void onLv3Selected(int position) {
        Area area = lv3().get(position);
        lv3Id = area.id;
        areaNames[2] = area.name;

        lv3Position = position;
        notifyChange();
    }


    @Override
    public int getItemLayoutId() {
        return R.layout.item_hospital;
    }

    public HashMap<String, Object> toJsonAnswer() {
        HashMap<String, Object> result = new HashMap<>();
        String[] type = new String[3];
        String[] content = new String[3];

        type[0] = String.valueOf(lv1Id);
        content[0] = getLv1Content();

        type[1] = String.valueOf(lv2Id);
        content[1] = getLv2Content();

        type[2] = String.valueOf(lv3Id);
        content[2] = getLv3Content();

        result.put("type", type);
        result.put("content", content);


        return result;
    }

    public ArrayList<OptionsPair> toJsonAnswer2() {
        ArrayList<OptionsPair> result = new ArrayList<>();
        OptionsPair optionsPair = new OptionsPair();
        optionsPair.setKey(String.valueOf(lv1Id));
        optionsPair.setValue(getLv1Content());
        result.add(optionsPair);

        OptionsPair optionsPair2 = new OptionsPair();
        optionsPair2.setKey(String.valueOf(lv2Id));
        optionsPair2.setValue(getLv2Content());
        result.add(optionsPair2);

        OptionsPair optionsPair3 = new OptionsPair();
        optionsPair3.setKey(String.valueOf(lv3Id));
        optionsPair3.setValue(getLv3Content());
        result.add(optionsPair3);

        return result;
    }


    public String getLv1Content() {
        return areaNames[0];
    }

    public String getLv2Content() {
        return areaNames[1];
    }

    public String getLv3Content() {
        return areaNames[2];
    }


    public int getLv3Position() {
        return lv3Position;
    }

    public int getLv2Position() {
        return lv2Position;
    }

    public int getLv1Position() {
        return lv1Position;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_hospital;
    }

    public String lastAnswerContent() {
        return areaNames[0] + areaNames[1] + areaNames[2];
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("question_id", getKey().replace(QuestionType.asel, ""));
        result.put("fill_content", toJsonAnswer2());
        return result;
    }
}
