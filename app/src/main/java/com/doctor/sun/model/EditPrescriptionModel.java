package com.doctor.sun.model;

import android.app.Activity;
import android.databinding.Observable;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.DrugAutoComplete;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.event.HideKeyboardEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.module.AutoComplete;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.NumberPickerDialog;
import com.doctor.sun.vm.ItemAutoCompleteTextInput;
import com.doctor.sun.vm.ItemRadioDialog;
import com.doctor.sun.vm.ItemTextInput2;
import com.doctor.sun.vm.validator.Validator;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by rick on 26/9/2016.
 */

public class EditPrescriptionModel {

    public List<SortedItem> parseData(Activity context, Prescription data, boolean isReadOnly) {
        if (data == null) {
            data = PrescriptionHandler.newInstance();
        }
        List<SortedItem> result = new ArrayList<>();

        final ItemAutoCompleteTextInput<DrugAutoComplete> name = new ItemAutoCompleteTextInput<>(R.layout.item_auto_complete_text, "药名", "");

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
        name.setEnabled(!isReadOnly);
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        name.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugAutoComplete drugAutoComplete = name.getFilteredEntries().get(position);
                name.setResult(drugAutoComplete.drugName);
//                productName.setResult(drugAutoComplete.productName);
                name.dismissDialog();
                EventHub.post(new HideKeyboardEvent());
            }
        });

        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 takeMedicineDays = new ItemTextInput2(R.layout.item_take_medicine_days, "用药天数", "");
        takeMedicineDays.setItemId("take_medicine_days");
        takeMedicineDays.setResult(data.getTake_medicine_days().equals("") ? "28" : data.getTake_medicine_days());
        takeMedicineDays.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        takeMedicineDays.setEnabled(!isReadOnly);
        takeMedicineDays.setClickable(!isReadOnly);
        final NumberPickerDialog dialog = new NumberPickerDialog(context, 1, 28);
        if (!data.getTake_medicine_days().equals("")) {
            dialog.setValue(Integer.parseInt(data.getTake_medicine_days()));
        }
        dialog.setConfirm(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeMedicineDays.setResult(String.valueOf(dialog.getValue()));
                dialog.dismiss();
            }
        });
        takeMedicineDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        if (Settings.isDoctor()) {
            result.add(takeMedicineDays);
            ModelUtils.insertDividerMarginLR(result);
        }


        final ItemRadioDialog.TextEvaluator evaluator = new ItemRadioDialog.TextEvaluator();

        ItemRadioDialog interval = new ItemRadioDialog(R.layout.item_pick_title);
        interval.setEvaluator(evaluator);
        interval.setSelectedItem(-1);
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
        interval.setEnabled(!isReadOnly);
        result.add(interval);

        ModelUtils.insertDividerMarginLR(result);

        final ItemRadioDialog unit = new ItemRadioDialog(R.layout.item_pick_title);
        unit.setEvaluator(evaluator);
        unit.setSelectedItem(-1);
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
        unit.setEnabled(!isReadOnly);
        result.add(unit);

        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 morning = new ItemTextInput2(R.layout.item_number_input, "早:    ", "");
        morning.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        morning.setSpan(6);
        morning.setItemId("morning");
        morning.setResult(data.getMorning());
        morning.setEnabled(!isReadOnly);
        result.add(morning);


        final ItemTextInput2 afternoon = new ItemTextInput2(R.layout.item_number_input, "午:    ", "");
        afternoon.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        afternoon.setSpan(6);
        afternoon.setItemId("noon");
        afternoon.setResult(data.getNoon());
        afternoon.setEnabled(!isReadOnly);
        result.add(afternoon);

        ModelUtils.insertDividerMarginLR(result);

        //这里接口晚上是用的night，但是实际上，晚上的英文翻译是evening。所以这里代码没有错，不要怀疑
        final ItemTextInput2 evening = new ItemTextInput2(R.layout.item_number_input, "晚:    ", "");
        evening.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        evening.setSpan(6);
        evening.setItemId("night");
        evening.setResult(data.getNight());
        evening.setEnabled(!isReadOnly);
        result.add(evening);


        final ItemTextInput2 night = new ItemTextInput2(R.layout.item_number_input, "睡前:", "");
        night.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        night.setSpan(6);
        night.setItemId("before_sleep");
        night.setResult(data.getBefore_sleep());
        night.setEnabled(!isReadOnly);
        result.add(night);

        final NumberValidator validator = new NumberValidator();
        morning.add(validator);
        afternoon.add(validator);
        evening.add(validator);
        night.add(validator);

        ModelUtils.insertDividerMarginLR(result);

        unit.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                morning.setSubTitle(unit.getSelectedItemText());
                afternoon.setSubTitle(unit.getSelectedItemText());
                evening.setSubTitle(unit.getSelectedItemText());
                night.setSubTitle(unit.getSelectedItemText());
            }
        });

        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_input2, "备注消息", "");
        remark.setItemId("remark");
        remark.setMaxLength(48);
        remark.setResult(data.getRemark());
        remark.setEnabled(!isReadOnly);
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
            return "处方份量格式错误";
        }

        boolean isValidNumber(String input) {
            return !input.equals(".");
        }
    }

}
