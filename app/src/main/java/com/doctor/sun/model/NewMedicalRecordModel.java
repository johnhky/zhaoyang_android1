package com.doctor.sun.model;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;
import com.doctor.sun.vm.ItemPickDate;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordModel {

    public NewMedicalRecordModel() {

    }

    public List<SortedItem> parseItem(int recordType) {
        List<SortedItem> result = new ArrayList<>();

        if (recordType == NewMedicalRecordFragment.TYPE_OTHER) {

            ItemTextInput2 relation = new ItemTextInput2(R.layout.item_text_input2, "您是患者的", "必填");
            relation.setResultNotEmpty();
            relation.setItemId("relation");
            relation.setPosition(result.size());
            result.add(relation);

            ModelUtils.insertDividerMarginLR(result);

            if (!Settings.haveSelfName()) {
                ItemTextInput2 selfName = new ItemTextInput2(R.layout.item_text_input2, "您的姓名", "必填");
                selfName.setResultNotEmpty();
                selfName.setItemId("selfName");
                selfName.setPosition(result.size());
                result.add(selfName);

                ModelUtils.insertDividerMarginLR(result);
            }
        }

        ItemTextInput2 name;
        if (recordType == NewMedicalRecordFragment.TYPE_SELF) {
            name = new ItemTextInput2(R.layout.item_text_input2, "姓名", "必填");
            name.setItemId("selfName");
        } else {
            name = new ItemTextInput2(R.layout.item_text_input2, "患者姓名", "必填");
            name.setItemId("name");
        }
        name.setResultNotEmpty();
        name.setPosition(result.size());
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        ItemPickDate date = new ItemPickDate(R.layout.item_pick_date2, "出生年月");
        date.setItemId("birthday");
        date.setPosition(result.size());
        result.add(date);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioGroup genderGroup = new ItemRadioGroup(R.layout.item_pick_gender);
        genderGroup.setTitle("性别");
        genderGroup.setResultNotEmpty();
        genderGroup.setItemId("gender");
        genderGroup.setPosition(result.size());
        result.add(genderGroup);

        ModelUtils.insertDividerMarginLR(result);

        return result;
    }


    public void saveMedicalRecord(SortedListAdapter adapter, int recordType, Callback<ApiDTO<MedicalRecord>> callback) {
        HashMap<String, String> medicalRecord = ModelUtils.toHashMap(adapter, callback);
        if (medicalRecord != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            if (recordType == NewMedicalRecordFragment.TYPE_SELF) {
                api.setSelfMedicalRecord(medicalRecord).enqueue(callback);
            } else {
                api.setRelativeMedicalRecord(medicalRecord).enqueue(callback);
            }

        }
    }

}