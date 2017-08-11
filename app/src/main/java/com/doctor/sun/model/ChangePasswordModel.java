package com.doctor.sun.model;

import android.text.InputType;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vm.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kb on 16-12-8.
 */

public class ChangePasswordModel {

    public List<SortedItem> parseData() {
        List<SortedItem> items = new ArrayList<>();

        ItemTextInput2 password = new ItemTextInput2(R.layout.item_text_input5, "旧密码", "");
        password.setResultNotEmpty();
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        password.setItemId("password");
        password.setPosition(items.size());
        items.add(password);

        ModelUtils.insertDividerNoMargin(items);

        String hint = "请输入6-10个数字与英文字符的组合";
        ItemTextInput2 newPassword = new ItemTextInput2(R.layout.item_text_input5, "新密码", hint);
        newPassword.setResultNotEmpty();
        newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        newPassword.setItemId("newPassword");
        newPassword.setPosition(items.size());
        items.add(newPassword);

        ModelUtils.insertDividerNoMargin(items);

        ItemTextInput2 confirmPassword = new ItemTextInput2(R.layout.item_text_input5, "确认新密码", "");
        confirmPassword.setResultNotEmpty();
        confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        confirmPassword.setItemId("confirmPassword");
        confirmPassword.setPosition(items.size());
        items.add(confirmPassword);

        ModelUtils.insertDividerNoMargin(items);



        return items;
    }

    public void save(SortedListAdapter adapter, SimpleCallback callback) {
        HashMap<String, String> result = ModelUtils.passwordToHashMap(adapter, callback);
        if (result != null) {
            ProfileModule api = Api.of(ProfileModule.class);
            api.resetPassword(result).enqueue(callback);
        }
    }
}
