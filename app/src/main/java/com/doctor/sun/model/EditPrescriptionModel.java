package com.doctor.sun.model;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DrugAutoComplete;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.module.AutoComplete;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.ItemAutoCompleteTextInput;
import com.doctor.sun.vo.ItemRadioDialog;
import com.doctor.sun.vo.ItemTextInput2;
import com.doctor.sun.vo.validator.Validator;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by rick on 26/9/2016.
 */

public class EditPrescriptionModel {

    public List<SortedItem> parseData(Prescription data) {
        if (data == null) {
            data = PrescriptionHandler.newInstance();
        }
        List<SortedItem> result = new ArrayList<>();


        final ItemAutoCompleteTextInput<DrugAutoComplete> name = new ItemAutoCompleteTextInput<>(R.layout.item_auto_complete_text, "药名/成分名", "");

        AutoComplete autoComplete = Api.of(AutoComplete.class);
        autoComplete.drugNames().enqueue(new SimpleCallback<List<DrugAutoComplete>>() {
            @Override
            protected void handleResponse(List<DrugAutoComplete> response) {
                name.setAllEntries(response);
            }

            @Override
            public void onFailure(Call<ApiDTO<List<DrugAutoComplete>>> call, Throwable t) {
                super.onFailure(call, t);
            }
        });

        name.setPredicate(new Predicate<DrugAutoComplete>() {
            @Override
            public boolean apply(DrugAutoComplete input) {
                return input.toString().contains(name.getResult());
            }
        });

        name.setSubTitle("(必填)");
        name.setResultNotEmpty();
        name.setItemId("drug_name");
        name.setResult(data.getDrug_name());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 productName = new ItemTextInput2(R.layout.item_text_input2, "商品名", "");
        productName.setSubTitle("(选填)");
        productName.setItemId("scientific_name");
        productName.setResult(data.getScientific_name());
        result.add(productName);

        name.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugAutoComplete drugAutoComplete = name.getFilteredEntries().get(position);
                name.setResult(drugAutoComplete.drugName);
                productName.setResult(drugAutoComplete.productName);
                name.dismissDialog();
            }
        });


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

        final ItemTextInput2 morning = new ItemTextInput2(R.layout.item_text_input2, "早", "");
        morning.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        morning.setSpan(6);
        morning.setItemId("morning");
        morning.setResult(data.getMorning());

        result.add(morning);

        final ItemTextInput2 afternoon = new ItemTextInput2(R.layout.item_text_input2, "午", "");
        afternoon.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        afternoon.setSpan(6);
        afternoon.setItemId("noon");
        afternoon.setResult(data.getNoon());
        result.add(afternoon);

        ModelUtils.insertDividerMarginLR(result);

        //这里接口晚上是用的night，但是实际上，表示晚上的是evening。所以。。不要怀疑
        final ItemTextInput2 evening = new ItemTextInput2(R.layout.item_text_input2, "晚", "");
        evening.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        evening.setSpan(6);
        evening.setItemId("night");
        evening.setResult(data.getNight());
        result.add(evening);

        final ItemTextInput2 night = new ItemTextInput2(R.layout.item_text_input2, "睡前", "");
        night.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        night.setSpan(6);
        night.setItemId("before_sleep");
        night.setResult(data.getBefore_sleep());
        result.add(night);

        final NumberValidator validator = new NumberValidator();
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

        @Override
        public boolean isValid(String input) {
            return isValidNumber(input);
        }


        @Override
        public String errorMsg() {
            return "用药份量格式错误";
        }

        boolean isValidNumber(String input) {
            return !input.equals(".");
        }
    }

}
