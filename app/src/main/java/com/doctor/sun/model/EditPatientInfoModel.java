package com.doctor.sun.model;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ChangeMyPhoneNumFragment;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemPickImage;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by kb on 16-9-26.
 */

public class EditPatientInfoModel {

    public List<SortedItem> parseData(Patient data) {
        if (data == null) {
            data = new Patient();
        }

        List<SortedItem> result = new ArrayList<>();

        ItemPickImage avatar = new ItemPickImage(R.layout.item_pick_avatar, data.getAvatar());
        avatar.setItemId("avatar");
        avatar.setPosition(result.size());
        avatar.setEnabled(false);
        result.add(avatar);

        ModelUtils.insertDividerNoMargin(result);

        ItemTextInput2 name = new ItemTextInput2(R.layout.item_text_input2, "姓名", "");
        name.setResult(data.getName());
        name.setResultNotEmpty();
        name.setItemId("name");
        name.setPosition(result.size());
        name.setEnabled(false);
        result.add(name);

        ModelUtils.insertDividerMarginLR(result);

        if (data.getPhone() == null || data.getPhone().equals("")) {
            ItemTextInput2 phone = ItemTextInput2.mobilePhoneInput("手机号码", "请输入11位手机号码");
            phone.setResultNotEmpty();
            phone.setItemLayoutId(R.layout.item_text_input2);
            phone.setItemId("phone");
            phone.setPosition(result.size());
            phone.setResult(data.getPhone());
            result.add(phone);
        } else {
            ClickMenu changePhone = new ClickMenu(R.layout.item_change_phone_num, 0, "手机号码", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeMyPhoneNumFragment.startFrom(v.getContext());
                }
            });
            changePhone.setSubTitle("更换手机号码");
            changePhone.setDetail(data.getPhone());
            changePhone.setPosition(result.size());
            result.add(changePhone);
        }

        ModelUtils.insertDividerMarginLR(result);

        ItemTextInput2 email = new ItemTextInput2(R.layout.item_text_input2, "邮箱", "选填");
        email.setResult(data.getEmail());
        email.setItemId("email");
        email.setPosition(result.size());
        email.setEnabled(false);
        result.add(email);

        ModelUtils.insertDividerMarginLR(result);

        ItemPickDate birthday = new ItemPickDate(R.layout.item_pick_date2, "出生年月");
        birthday.setDate(data.getBirthday());
        birthday.setItemId("birthday");
        birthday.setPosition(result.size());
        birthday.setEnabled(false);
        result.add(birthday);

        ModelUtils.insertDividerMarginLR(result);

        ItemRadioGroup radioGroup = new ItemRadioGroup(R.layout.item_pick_gender);
        radioGroup.setTitle("性别");
        radioGroup.setResultNotEmpty();
        radioGroup.setItemId("gender");
        radioGroup.setPosition(result.size());
        radioGroup.setSelectedItem(data.getGender());
        radioGroup.setEnabled(false);
        result.add(radioGroup);

        ModelUtils.insertDividerMarginLR(result);

        return result;
    }

    public void savePatientInfo(SortedListAdapter adapter, Callback<ApiDTO<Patient>> callback) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.editPatientInfo(adapter.get("name").getValue(),
                adapter.get("email").getValue(),
                adapter.get("birthday").getValue(),
                Integer.parseInt(adapter.get("gender").getValue()),
                adapter.get("avatar").getValue()).enqueue(callback);
    }
}
