package com.doctor.sun.model;

import android.content.Context;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.NewMedicalRecordFragment;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordModel {

    private Context context;

    public NewMedicalRecordModel(Context context) {
        this.context = context;
    }

    public List<SortedItem> parseItem(int recordType) {
        List<SortedItem> result = new ArrayList<>();

        Description warningDescription = new Description(R.layout.item_description_record_warning, context.getResources().getString(R.string.record_warning));
        warningDescription.setItemId("warningDescription");
        warningDescription.setPosition(result.size());
        result.add(warningDescription);

        if (recordType == NewMedicalRecordFragment.TYPE_OTHER) {

            ItemTextInput2 relation = new ItemTextInput2(R.layout.item_text_input2, "您是患者的", "必填");
            relation.setResultNotEmpty();
            relation.setItemId("relation");
            relation.setPosition(result.size());
            result.add(relation);

            ModelUtils.insertDivider(result);

            ItemTextInput2 selfName = new ItemTextInput2(R.layout.item_text_input2, "您的姓名", "必填");
            selfName.setResultNotEmpty();
            selfName.setItemId("selfName");
            selfName.setPosition(result.size());
            result.add(selfName);

            ModelUtils.insertDivider(result);
        }

        ItemTextInput2 name;
        if (recordType == NewMedicalRecordFragment.TYPE_SELF) {
            name = new ItemTextInput2(R.layout.item_text_input2, "姓名", "必填");
        } else {
            name = new ItemTextInput2(R.layout.item_text_input2, "患者姓名", "必填");
        }
        name.setResultNotEmpty();
        name.setItemId("selfName");
        name.setPosition(result.size());
        result.add(name);

        ModelUtils.insertDivider(result);

        ItemTextInput2 email = new ItemTextInput2(R.layout.item_text_input2, "邮箱", "选填");
        email.setItemId("email");
        email.setPosition(result.size());
        result.add(email);

        ModelUtils.insertDivider(result);

        ItemPickDate date = new ItemPickDate(R.layout.item_pick_date2, "出生年月");
        date.setItemId("birthday");
        date.setPosition(result.size());
        result.add(date);

        ModelUtils.insertDivider(result);

        ItemRadioGroup genderGroup = new ItemRadioGroup(R.layout.item_pick_gender);
        genderGroup.setTitle("性别");
        genderGroup.setResultNotEmpty();
        genderGroup.setItemId("gender");
        genderGroup.setPosition(result.size());
        result.add(genderGroup);

        ModelUtils.insertDivider(result);

        ItemTextInput2 idNumber = new ItemTextInput2(R.layout.item_text_input2, "身份证号码", "选填");
        idNumber.setItemId("identityNumber");
        idNumber.setPosition(result.size());
        result.add(idNumber);

        ModelUtils.insertDivider(result);

        return result;
    }

    public void saveMedicalRecord(SortedListAdapter adapter, int recordType, Callback<ApiDTO<String>> callback) {
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
