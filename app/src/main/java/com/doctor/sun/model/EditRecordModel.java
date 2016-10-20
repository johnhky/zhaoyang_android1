package com.doctor.sun.model;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-26.
 */

public class EditRecordModel {

    public List<SortedItem> parseItems(MedicalRecord data) {
        if (data == null) {
            data = new MedicalRecord();
        }

        List<SortedItem> result = new ArrayList<>();

        ItemTextInput2 relation = new ItemTextInput2(R.layout.item_text_input2, "您是患者的", "必填");
        relation.setEnabled(false);
        relation.setResult(data.getRelation());
        relation.setResultNotEmpty();
        relation.setItemId("relation");
        relation.setPosition(result.size());
        result.add(relation);

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "患者姓名", "必填");
        name.setEnabled(false);
        name.setResult(data.getRecordName());
        name.setResultNotEmpty();
        name.setItemId("name");
        name.setPosition(result.size());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        ItemPickDate birthday = new ItemPickDate(R.layout.item_pick_date2, "出生年月");
        birthday.setEnabled(false);
        birthday.setResultNotEmpty();
        birthday.setDate(data.getBirthday());
        birthday.setItemId("birthday");
        birthday.setPosition(result.size());
        result.add(birthday);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioGroup gender = new ItemRadioGroup(R.layout.item_pick_gender);
        gender.setEnabled(false);
        gender.setTitle("性别");
        gender.setSelectedItem(data.getGender());
        gender.setItemId("gender");
        gender.setPosition(result.size());
        result.add(gender);

        ModelUtils.insertDividerMarginLR(result);

        return result;
    }

    public void saveRecord(MedicalRecord medicalRecord, Callback<ApiDTO<MedicalRecord>> callback) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.editMedicalRecord(medicalRecord.toHashMap()).enqueue(callback);
    }

}
