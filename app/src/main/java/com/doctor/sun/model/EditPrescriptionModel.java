package com.doctor.sun.model;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vo.ItemRadioDialog;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 26/9/2016.
 */

public class EditPrescriptionModel {

    public List<SortedItem> parseData(Prescription data) {
        if (data == null) {
            data = new Prescription();
        }
        List<SortedItem> result = new ArrayList<>();


        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "药名/成分名", "");
        name.setSubTitle("(必填)");
        name.setResultNotEmpty();
        name.setItemId("drug_name");
        name.setResult(data.getName());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 productName = new ItemTextInput2(R.layout.item_text_input2, "商品名", "");
        productName.setSubTitle("(选填)");
        productName.setItemId("scientific_name");
        productName.setResult(data.getDrugName());
        result.add(productName);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioDialog unit = new ItemRadioDialog(R.layout.item_pick_title);
        unit.setResultNotEmpty();
        unit.setTitle("单位");
        unit.setItemId("drug_unit");
        unit.setPosition(result.size());
        String[] units = AppContext.me().getResources().getStringArray(R.array.unit_array);
        unit.addOptions(units);
        result.add(unit);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioDialog interval = new ItemRadioDialog(R.layout.item_pick_title);
        interval.setResultNotEmpty();
        interval.setTitle("间隔");
        interval.setItemId("frequency");
        interval.setPosition(result.size());
        String[] intervals = AppContext.me().getResources().getStringArray(R.array.interval_array);
        interval.addOptions(intervals);
        result.add(interval);


        result.add(new Description(R.layout.item_description, "数量"));

        ItemTextInput2 morning = new ItemTextInput2(R.layout.item_text_input2, "早", "");
        morning.setSpan(6);
        morning.setSubTitle("(选填)");
        morning.setItemId("morning");
        morning.setResult(data.getDrugName());
        result.add(morning);

        ItemTextInput2 afternoon = new ItemTextInput2(R.layout.item_text_input2, "午", "");
        afternoon.setSubTitle("(选填)");
        afternoon.setSpan(6);
        afternoon.setItemId("noon");
        afternoon.setResult(data.getDrugName());
        result.add(afternoon);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 night = new ItemTextInput2(R.layout.item_text_input2, "晚", "");
        night.setSubTitle("(选填)");
        night.setSpan(6);
        night.setItemId("night");
        night.setResult(data.getDrugName());
        result.add(night);

        ItemTextInput2 evening = new ItemTextInput2(R.layout.item_text_input2, "睡前", "");
        evening.setSubTitle("(选填)");
        evening.setSpan(6);
        evening.setItemId("before_sleep");
        evening.setResult(data.getDrugName());
        result.add(evening);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_input2, "备注消息", "");
        remark.setSubTitle("备注消息");
        remark.setItemId("remark");
        remark.setResult(data.getDrugName());
        result.add(remark);
        return result;
    }
}
