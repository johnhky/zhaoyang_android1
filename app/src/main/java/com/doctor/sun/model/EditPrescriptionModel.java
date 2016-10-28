package com.doctor.sun.model;

import android.databinding.Observable;
import android.util.SparseBooleanArray;
import android.view.inputmethod.EditorInfo;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.ItemRadioDialog;
import com.doctor.sun.vo.ItemTextInput2;
import com.doctor.sun.vo.validator.Validator;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 26/9/2016.
 */

public class EditPrescriptionModel {

    public List<SortedItem> parseData(Prescription data) {
        if (data == null) {
            data = PrescriptionHandler.newInstance();
        }
        List<SortedItem> result = new ArrayList<>();


        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "药名/成分名", "");
        name.setSubTitle("(必填)");
        name.setResultNotEmpty();
        name.setItemId("drug_name");
        name.setResult(data.getDrug_name());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 productName = new ItemTextInput2(R.layout.item_text_input2, "商品名", "");
        productName.setSubTitle("(选填)");
        productName.setItemId("scientific_name");
        productName.setResult(data.getScientific_name());
        result.add(productName);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioDialog.TextEvaluator evaluator = new ItemRadioDialog.TextEvaluator();
        ItemRadioDialog unit = new ItemRadioDialog(R.layout.item_pick_title);
        unit.setEvaluator(evaluator);
        unit.setSelectedItem(0);
        unit.setResultNotEmpty();
        unit.setTitle("单位");
        unit.setItemId("drug_unit");
        unit.setPosition(result.size());
        String[] units = AppContext.me().getResources().getStringArray(R.array.unit_array);
        unit.addOptions(units);
        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(data.getDrug_unit())) {
                unit.setSelectedItem(i);
            }
        }
        result.add(unit);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioDialog interval = new ItemRadioDialog(R.layout.item_pick_title);
        interval.setEvaluator(evaluator);
        interval.setSelectedItem(0);
        interval.setResultNotEmpty();
        interval.setTitle("间隔");
        interval.setItemId("frequency");
        interval.setPosition(result.size());
        String[] intervals = AppContext.me().getResources().getStringArray(R.array.interval_array);
        interval.addOptions(intervals);
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i].equals(data.getFrequency())) {
                interval.setSelectedItem(i);
            }
        }
        result.add(interval);


        result.add(new Description(R.layout.item_description, "数量"));

        final NumberValidator validator = new NumberValidator();
        final ItemTextInput2 morning = new ItemTextInput2(R.layout.item_text_input2, "早", "");
        morning.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        morning.setSpan(6);
        morning.setItemId("morning");
        morning.setResult(data.getMorning());
        morning.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                validator.put(0, isValidNumber(morning.getValue()));
            }
        });
        result.add(morning);

        final ItemTextInput2 afternoon = new ItemTextInput2(R.layout.item_text_input2, "午", "");
        afternoon.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        afternoon.setSpan(6);
        afternoon.setItemId("noon");
        afternoon.setResult(data.getNoon());
        afternoon.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                validator.put(1, isValidNumber(afternoon.getValue()));
            }
        });
        result.add(afternoon);

        ModelUtils.insertDividerMarginLR(result);

        //这里接口晚上是用的night，但是实际上，表示晚上的是evening。所以。。不要怀疑
        final ItemTextInput2 evening = new ItemTextInput2(R.layout.item_text_input2, "晚", "");
        evening.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        evening.setSpan(6);
        evening.setItemId("night");
        evening.setResult(data.getNight());
        evening.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                validator.put(2, isValidNumber(evening.getValue()));
            }
        });
        result.add(evening);

        final ItemTextInput2 night = new ItemTextInput2(R.layout.item_text_input2, "睡前", "");
        night.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        night.setSpan(6);
        night.setItemId("before_sleep");
        night.setResult(data.getBefore_sleep());
        night.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                validator.put(3, isValidNumber(night.getValue()));
            }
        });
        result.add(night);

        morning.add(validator);
        afternoon.add(validator);
        evening.add(validator);
        night.add(validator);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_input2, "备注消息", "");
        remark.setSubTitle("备注消息");
        remark.setItemId("remark");
        remark.setResult(data.getRemark());
        result.add(remark);
        return result;
    }

    public HashMap<String, String> save(SortedListAdapter adapter, SimpleCallback callback) {
        return ModelUtils.toHashMap(adapter, callback);
    }

    private static class NumberValidator implements Validator {
        SparseBooleanArray states = new SparseBooleanArray(4);

        public NumberValidator() {
        }


        public void put(int key, boolean value) {
            states.put(key, value);
        }

        @Override
        public boolean isValid(String input) {
            return states.get(0, false)
                    || states.get(1, false)
                    || states.get(2, false)
                    || states.get(3, false);
        }


        @Override
        public String errorMsg() {
            return "请填写药物服用份量";
        }
    }

    public boolean isValidNumber(String input) {
        return !Strings.isNullOrEmpty(input) && !input.equals(".");
    }
}
