package com.doctor.sun.model;

import android.app.Activity;
import android.content.Context;
import android.databinding.Observable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.entity.DrugAutoComplete;
import com.doctor.sun.entity.DrugInfo;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.event.HideKeyboardEvent;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.ItemAutoCompleteTextInput;
import com.doctor.sun.vm.ItemRadioDialog;
import com.doctor.sun.vm.ItemSwitch;
import com.doctor.sun.vm.ItemTextInput2;
import com.doctor.sun.vm.MgPerUnitInput;
import com.doctor.sun.vm.TitrationList;
import com.doctor.sun.vm.validator.Validator;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import io.ganguo.library.core.event.EventHub;
import io.realm.Realm;

/**
 * Created by rick on 26/9/2016.
 */

public class EditPrescriptionModel {
    Context context;

    public List<SortedItem> parseData(final Activity context, Prescription data, final boolean isReadOnly) {
        this.context = context;
        if (data == null) {
            data = PrescriptionHandler.newInstance();
        }

        List<SortedItem> result = new ArrayList<>();
        final ItemAutoCompleteTextInput<DrugAutoComplete> name = new ItemAutoCompleteTextInput<>(R.layout.item_auto_complete_text, "药名", "");

        name.setSubTitle("(必填)");
        name.setResultNotEmpty();
        name.setItemId("drug_name");
        name.setResult(data.getDrug_name());
        name.setEnabled(!isReadOnly);
        name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (null!=name.arrayAdapter){
                    name.arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        name.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (name.list.size()>0){
                            name.setResult(name.list.get(position).getDrug_name());
                            name.dismissDialog();
                            name.arrayAdapter.notifyDataSetChanged();
                        }
            }
        });
        if (!TextUtils.isEmpty(name.getResult())) {
            name.dismissByUser = true;
        }
        result.add(name);
        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 takeMedicineDays = new ItemTextInput2(R.layout.item_take_medicine_days, "用药天数", "");
        takeMedicineDays.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        takeMedicineDays.setItemId("take_medicine_days");
        takeMedicineDays.setResult(data.getTake_medicine_days().equals("") ? "28" : data.getTake_medicine_days());
        takeMedicineDays.setEnabled(!isReadOnly);
        takeMedicineDays.setSpan(6);
        if (Settings.isDoctor()) {
            takeMedicineDays.setResultNotEmpty();
            result.add(takeMedicineDays);
            ModelUtils.insertDividerMarginLR(result);
        }
        final ItemSwitch itemSwitch = new ItemSwitch(R.layout.item_titration);
        itemSwitch.setContent("点击打开");

        if (data.getTitration().size() == 0) {
            itemSwitch.setChecked(false);
        } else {
            itemSwitch.setChecked(true);
        }
        itemSwitch.setItemId(UUID.randomUUID().toString());
        itemSwitch.setEnabled(!isReadOnly);

        if (Settings.isDoctor()) {
            if (AppContext.getInstance().getType() != AppointmentType.FollowUp) {
                result.add(itemSwitch);
            }
        }

        final ItemRadioDialog.TextEvaluator evaluator = new ItemRadioDialog.TextEvaluator();

        final ItemRadioDialog interval = new ItemRadioDialog(R.layout.item_pick_title);
        interval.setEvaluator(evaluator);
        interval.setSelectedItem(-1);
        interval.setTitle("间隔");
        interval.setItemId("frequency");
        interval.setResultNotEmpty();
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
        final String[] defaultUnits = AppContext.me().getResources().getStringArray(R.array.unit_array);

        unit.setEnabled(!isReadOnly);
        result.add(unit);

        ModelUtils.insertDividerMarginLR(result);

        final ItemTextInput2 morning = new ItemTextInput2(R.layout.item_number_input, "早:    ", "");
        morning.setSpan(6);
        morning.setItemId("morning");
        morning.setResult(data.getMorning());
        morning.setEnabled(!isReadOnly);
        result.add(morning);


        final ItemTextInput2 afternoon = new ItemTextInput2(R.layout.item_number_input, "午:    ", "");
        afternoon.setSpan(6);
        afternoon.setItemId("noon");
        afternoon.setResult(data.getNoon());
        afternoon.setEnabled(!isReadOnly);
        result.add(afternoon);
        ModelUtils.insertDividerMarginLR(result);

        //这里接口晚上是用的night，但是实际上，晚上的英文翻译是evening。所以这里代码没有错，不要怀疑
        final ItemTextInput2 evening = new ItemTextInput2(R.layout.item_number_input, "晚:    ", "");
        evening.setSpan(6);
        evening.setItemId("night");
        evening.setResult(data.getNight());
        evening.setEnabled(!isReadOnly);
        result.add(evening);


        final ItemTextInput2 night = new ItemTextInput2(R.layout.item_number_input, "睡前:", "");
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

        final MgPerUnitInput mUnit = new MgPerUnitInput(R.layout.item_mg_per_unit, "单位含量", "");
        mUnit.setSpan(12);
        mUnit.getDialog().setSelectedItem(-1);
        mUnit.setItemId("mg_per_unit");
        mUnit.setEnabled(!isReadOnly);
        if (Settings.isDoctor()) {
            if (mUnit.getDialog().getSelectedItem() == 1 || mUnit.getDialog().getSelectedItem() == 0) {
                mUnit.setResultNotEmpty();
            }
            result.add(mUnit);
            ModelUtils.insertDividerMarginLR(result);
        }

        final TitrationList titrationList = new TitrationList(R.layout.item_titrationlist, "用法用量", unit.getSelectedItem() + "");
        titrationList.setItemId("titration");
        result.add(titrationList);
        if (!isReadOnly) {
            titrationList.setEnabled(true);
        } else {
            titrationList.setEnabled(false);
        }
        if (data.getTitration() != null && data.getTitration().size() > 0) {
            interval.setVisible(false);
            morning.setVisible(false);
            afternoon.setVisible(false);
            night.setVisible(false);
            evening.setVisible(false);
            titrationList.setVisible(true);
            titrationList.setSubTitle(data.getDrug_unit());
            titrationList.addTitrations(data.getTitration());
        } else {
            titrationList.setVisible(false);
        }

        name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            private boolean isFirstTime = true;

            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.result && !isFirstTime) {
                    isFirstTime = false;
                    List<DrugInfo> filteredEntries = name.getFilteredEntries();
                    if (filteredEntries.size() > 0 && !Strings.isNullOrEmpty(name.getResult())) {
                        List<String> drugUnit = filteredEntries.get(0).getDrug_unit();
                        setDrugUnits(unit, drugUnit, defaultUnits, mUnit, filteredEntries.get(0).getSpecification());
                    } else {
                        setDrugUnits(unit, new ArrayList<String>(), defaultUnits, mUnit, "0");
                    }
                }
            }
        });

        name.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrugInfo drugAutoComplete = name.getFilteredEntries().get(position);
                List<String> drugUnit = drugAutoComplete.getDrug_unit();
                String specification = drugAutoComplete.getSpecification();
                name.setResult(drugAutoComplete.getDrug_name());
   /*             if (name.getResult().equals("奥氮平片(欧兰宁)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("利培酮片(维思通)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("奥氮平片(奥兰之)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("奥氮平口崩片(再普乐口崩片)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("奥氮平片(再普乐)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("盐酸文拉法辛缓释胶囊(怡诺思)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }
                if (name.getResult().equals("草酸艾司西酞普兰片(百适可)")) {
                    if (drugUnit.size() == 4) {
                        drugUnit.remove(2);
                        if (drugUnit.size() == 3) {
                            drugUnit.remove(2);
                        }
                    }
                }*/
                setDrugUnits(unit, drugUnit, defaultUnits, mUnit, specification);
                name.dismissDialog();
                EventHub.post(new HideKeyboardEvent());
            }
        });
        /*When I wrote this code,only God and I understood what I was doing,
        mabey a month or a few months later,only God understood*/
        itemSwitch.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSwitch.isChecked() == false) {
                    itemSwitch.setChecked(true);
                    itemSwitch.setContent("点击关闭");
                    interval.setVisible(false);
                    morning.setVisible(false);
                    afternoon.setVisible(false);
                    night.setVisible(false);
                    evening.setVisible(false);
                    titrationList.setVisible(true);
                    if (!isReadOnly) {
                        morning.setResult("");
                        afternoon.setResult("");
                        night.setResult("");
                        evening.setResult("");
                        interval.setSelectedItem(0);
                        titrationList.clearData();
                    }
                } else {
                    titrationList.adapter(context);
                    itemSwitch.setChecked(false);
                    itemSwitch.setContent("点击打开");
                    interval.setVisible(true);
                    morning.setVisible(true);
                    afternoon.setVisible(true);
                    night.setVisible(true);
                    evening.setVisible(true);
                    titrationList.setVisible(false);
                    if (unit.getSelectedItemText().equals("粒")) {
                        mUnit.setVisible(true);
                    } else {
                        mUnit.setVisible(false);
                    }
                }

            }
        });

        unit.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                morning.setSubTitle(unit.getSelectedItemText());
                afternoon.setSubTitle(unit.getSelectedItemText());
                evening.setSubTitle(unit.getSelectedItemText());
                night.setSubTitle(unit.getSelectedItemText());
                mUnit.setSubTitle(unit.getSelectedItemText());
                titrationList.setSubTitle(unit.getSelectedItemText());
                titrationList.setHint(unit.getSelectedItem() + "");
                boolean isStandardUnit = unit.getSelectedItemText().equals("克")
                        || unit.getSelectedItemText().equals("毫克");
                mUnit.setVisible(!isStandardUnit);
                if (!Settings.isDoctor()) {
                    if (unit.getSelectedItemText().equals("粒")) {
                        mUnit.setVisible(false);
                    } else {
                        mUnit.setVisible(true);
                    }
                }
            }
        });
        titrationList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (titrationList.getSimpleAdapter() != null) {
                    titrationList.getSimpleAdapter().notifyDataSetChanged();
                }
            }
        });
        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_input4, "备注消息", "");
        remark.setItemId("remark");
        remark.setMaxLength(48);
        remark.setResult(data.getRemark());
        remark.setEnabled(!isReadOnly);
        result.add(remark);

        loadAutoCompleteDrug(data, name, unit, defaultUnits);

        if (!TextUtils.isEmpty(data.getUnits())) {
            switch (data.getUnits()) {
                case "毫克": {
                    mUnit.getDialog().setSelectedItem(0);
                    break;
                }
                case "克": {
                    mUnit.getDialog().setSelectedItem(1);
                    break;

                }

                default: {
                }
            }
        }
        if (!TextUtils.isEmpty(data.getSpecification())) {
            if (data.getSpecification().equals("-1")) {
                mUnit.getDialog().setSelectedItem(2);
            } else {
                mUnit.setResult(data.getSpecification());
                mUnit.getDialog().setSelectedItem(0);
            }
        } else {
            if (!TextUtils.isEmpty(data.getDrug_unit())) {
                if (data.getDrug_unit().equals("粒")) {
                    mUnit.getDialog().setSelectedItem(0);
                }
            }
        }
        if (data.getTitration() != null && data.getTitration().size() != 0 && isReadOnly) {
            morning.setVisible(false);
            night.setVisible(false);
            interval.setVisible(false);
            afternoon.setVisible(false);
            evening.setVisible(false);
            itemSwitch.setChecked(true);
            itemSwitch.setEnabled(false);
            titrationList.setVisible(true);
        }
        return result;
    }

    private void loadAutoCompleteDrug(Prescription data, final ItemAutoCompleteTextInput<DrugAutoComplete> name, final ItemRadioDialog unit, final String[] defaultUnits) {
        unit.addOptions(defaultUnits);
        for (int i = 0; i < defaultUnits.length; i++) {
            if (defaultUnits[i].equals(data.getDrug_unit())) {
                unit.setSelectedItem(i);
            }
        }
   /*     AutoComplete autoComplete = Api.of(AutoComplete.class);
        autoComplete.drugDetail().enqueue(new SimpleCallback<List<DrugDetail>>() {
            @Override
            protected void handleResponse(final List<DrugDetail> response) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(response);
                        realm.close();
                    }

                });
            }
        });*/
    }

    private void setDrugUnits(ItemRadioDialog unit, List<String> drugUnit, String[] defaultUnits, MgPerUnitInput mgPerUnitInput, String specification) {
        if (drugUnit != null && drugUnit.size() > 0) {
            if (!unit.hasSameOptions(drugUnit)) {
                unit.clearOptions();
                unit.addOptions(drugUnit);
                if (unit.getOptions().size() == 1) {
                    unit.setSelectedItem(0);
                }
                for (int i = 0; i < drugUnit.size(); i++) {
                    if (unit.getOptions().get(i).equals("粒")) {
                        if (specification.equals("-1")) {
                            mgPerUnitInput.getDialog().setSelectedItem(2);
                        } else {
                            mgPerUnitInput.getDialog().setSelectedItem(0);
                        }
                    }
                }
                unit.notifyChange();
            }
        } else {
            if (!unit.hasSameOptions(defaultUnits)) {
                unit.clearOptions();
                unit.addOptions(defaultUnits);
                if (unit.getOptions().size() == 1) {
                    unit.setSelectedItem(0);
                }
                unit.notifyChange();
            }
        }
    }

    public HashMap<String, String> save(SortedListAdapter adapter, SimpleCallback callback) {
        HashMap<String, String> stringStringHashMap = ModelUtils.toHashMap(adapter, callback);
        if (stringStringHashMap != null) {
            String mg_per_unit = stringStringHashMap.remove("mg_per_unit");
            if (!Strings.isNullOrEmpty(mg_per_unit)) {
                String[] split = mg_per_unit.split(",");
                if (split[1].equals("ignored")) {
                    stringStringHashMap.put("units", "");
                } else {
                    stringStringHashMap.put("units", split[1]);
                }
                stringStringHashMap.put("specification", split[0]);
            }
            if (Settings.isDoctor()) {
                String titration = stringStringHashMap.get("titration");
                if (!TextUtils.isEmpty(titration)) {
                    stringStringHashMap.put("titration", titration.replace("titration:", ""));
                }
            }
        }
        return stringStringHashMap;
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
