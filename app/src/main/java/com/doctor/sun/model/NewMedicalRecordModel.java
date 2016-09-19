package com.doctor.sun.model;

import android.content.Context;
import android.content.pm.LauncherApps;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-18.
 */

public class NewMedicalRecordModel {

    private Context context;

    public NewMedicalRecordModel(Context context) {
        this.context = context;
    }

    public List<SortedItem> parseItem() {
        List<SortedItem> result = new ArrayList<>();

        Description warningDescription = new Description(R.layout.item_description_record_warning, context.getResources().getString(R.string.record_warning));
        warningDescription.setItemId("warningDescription");
        warningDescription.setPosition(result.size());
        result.add(warningDescription);

        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "姓名", "必填");
        name.setItemId("name");
        name.setPosition(result.size());
        result.add(name);

        ModelUtils.insertDivider(result);

        ItemTextInput2 email = new ItemTextInput2(R.layout.item_text_input2, "邮箱", "选填");
        email.setItemId("email");
        email.setPosition(result.size());
        result.add(email);

        ModelUtils.insertDivider(result);

        ItemPickDate date = new ItemPickDate(R.layout.item_pick_date2, "出生年月");
        date.setItemId("date");
        date.setResultNotEmpty();
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

        ItemTextInput2 idNumber = new ItemTextInput2(R.layout.item_text_input2, "身份证号", "选填");
        idNumber.setItemId("idNumber");
        idNumber.setPosition(result.size());
        result.add(idNumber);

        ModelUtils.insertDivider(result);

        return result;
    }

    public void saveMedicalRecord(SortedListAdapter adapter, Callback<ApiDTO<String>> callback) {
        HashMap<String, String> medicalRecord = ModelUtils.toHashMap(adapter, callback);
        if (medicalRecord != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            api.setSelfMedicalRecord(medicalRecord).enqueue(callback);
        }
    }
}
