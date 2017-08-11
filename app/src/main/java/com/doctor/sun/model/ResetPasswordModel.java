package com.doctor.sun.model;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.MD5;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ItemTextInput2;
import com.doctor.sun.vm.ItemTextView;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by heky on 17/6/26.
 */

public class ResetPasswordModel {

    public List<SortedItem>parseData(final FragmentActivity activity, final SortedListAdapter adapter){
        List<SortedItem>result = new ArrayList<>();
        String hint = "请输入6-10个数字与英文字符的组合";
        ItemTextInput2 password = new ItemTextInput2(R.layout.item_text_input5, "请设置密码", "");
        password.setResultNotEmpty();
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        password.setItemId("newPassword");
        password.setHint(hint);
        password.setPosition(result.size());
        result.add(password);
        ModelUtils.insertDividerNoMargin(result);
        ItemTextInput2 password2 = new ItemTextInput2(R.layout.item_text_input5, "确认密码", "");
        password2.setResultNotEmpty();
        password2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        password2.setItemId("confirmPassword");
        password2.setHint("请重复填写您输入的密码");
        password2.setPosition(result.size());
        result.add(password2);

        ModelUtils.insertDividerNoMargin(result);

        ModelUtils.insertSpace(result);

        ItemTextView textView = new ItemTextView(R.layout.item_button_truth, "");
        textView.setListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                save(adapter, new SimpleCallback() {
                    @Override
                    protected void handleResponse(Object response) {
                        Toast.makeText(activity,"密码设置成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Constants.PSW_UPDATE_SUCCESS);
                        activity.sendBroadcast(intent);
                        activity.finish();
                    }
                });
            }
        });
        textView.setPosition(result.size());
        result.add(textView);
        return result;
    }

    public void save(SortedListAdapter adapter, SimpleCallback callback){
        HashMap<String, String> result = toHashMap(adapter);
        if (result!=null){
            ProfileModule api = Api.of(ProfileModule.class);
            api.resetPassword(result).enqueue(callback);
        }
    }
    private HashMap<String, String> toHashMap(SortedListAdapter adapter) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                return null;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    if (key.equals("newPassword")) {
                        result.put(key, MD5.getMessageDigest(value.getBytes()));
                    }
                    if (key.equals("confirmPassword")){
                        result.put(key, MD5.getMessageDigest(value.getBytes()));
                    }
                }
            }
        }
        return result;
    }

}
