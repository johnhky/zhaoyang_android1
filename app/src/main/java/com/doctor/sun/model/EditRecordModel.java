package com.doctor.sun.model;

import android.text.InputType;
import android.util.Log;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.ItemPickDate;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kb on 16-9-26.
 */

public class EditRecordModel {
    private String[] relativeList = AppContext.me().getResources().getStringArray(R.array.relative_array);
    public List<SortedItem> parseItems(MedicalRecord data) {
        if (data == null) {
            data = new MedicalRecord();
        }

        List<SortedItem> result = new ArrayList<>();

        //careful , don't miss this field
        ItemTextInput2 id = new ItemTextInput2(R.layout.item_empty, "", "");
        id.setItemId("recordId");
        id.setResult(String.valueOf(data.getMedicalRecordId()));
        result.add(id);
        ItemTextInput2 relation = new ItemTextInput2(R.layout.item_record_text, "您是患者的", "必填");
        relation.setEnabled(false);
        relation.setResult(data.getRelation());
        relation.setResultNotEmpty();
        relation.setItemId("relation");
        relation.setPosition(result.size());
        boolean notEditable = "本人".equals(data.getRelation());
        relation.addOptions(relativeList);
        if (notEditable) {
            relation.setInputType(InputType.TYPE_NULL);
        }else{
            relation.removeOption(0);
        }
        result.add(relation);
        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input5, "患者姓名", "必填");
        name.setEnabled(false);
        name.setResult(data.getRecordName());
        name.setResultNotEmpty();
        name.setItemId("name");
        name.setPosition(result.size());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        ItemPickDate birthday = new ItemPickDate(R.layout.item_pick_birthmonth, "出生年月");
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

    public void saveRecord(SortedListAdapter medicalRecord, Callback<ApiDTO<MedicalRecord>> callback) {
        HashMap<String, String> data = ModelUtils.toHashMap(medicalRecord, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        if (data != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            api.editMedicalRecord(data).enqueue(callback);
        }
    }

}
