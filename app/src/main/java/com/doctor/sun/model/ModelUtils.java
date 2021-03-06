package com.doctor.sun.model;

import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.MD5;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.ItemTextInput2;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kb on 16-9-18.
 */

final class ModelUtils {

    static void insertDividerMarginLR(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_start13_end13);
        item.setItemId("DIVIDER" + list.size());
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertDividerNoMargin(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px);
        item.setItemId("DIVIDER" + list.size());
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertTitle(List<SortedItem> list, String title) {
        ItemTextInput2 item = newTitles(list, title);
        list.add(item);
    }

    @NonNull
    private static ItemTextInput2 newTitles(List<SortedItem> list, String title) {
        ItemTextInput2 item = new ItemTextInput2(R.layout.item_dialog_title, title, "");
        item.setItemId("DIVIDER" + list.size());
        item.setPosition(list.size());
        return item;
    }


    static void insertVerticalDivider(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_vertical_1px);
        item.setItemId("DIVIDER" + list.size());
        item.setSpan(2);
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertSpace(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_30dp);
        item.setItemId("SPACE" + list.size());
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertSpace(List<SortedItem> list, int layout) {
        BaseItem item = new BaseItem(layout);
        item.setItemId("SPACE" + list.size());
        item.setPosition(list.size());
        list.add(item);
    }

    static HashMap<String, String> toHashMap(SortedListAdapter adapter, Callback callback) {
        boolean isValid = true;
        HashMap<String, String> result = new HashMap<>();

        //遍历所有的item
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                isValid = false;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    result.put(key, value);
                }
            }
        }

        if (!isValid) {
            ApiDTO<String> body = new ApiDTO<>();
            body.setStatus("500");
            body.setMessage("请填写必填项目");
            if (callback != null) {
                callback.onResponse(null, Response.success(body));
            }
            return null;
        } else {
            return result;
        }
    }

    static HashMap<String, String> passwordToHashMap(SortedListAdapter adapter, Callback callback) {
        boolean isValid = true;
        HashMap<String, String> result = new HashMap<>();

        //遍历所有的item
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                isValid = false;
            }

            String value = MD5.getMessageDigest(item.getValue().getBytes());
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    result.put(key, value);
                }
            }
        }

        if (!isValid) {
            ApiDTO<String> body = new ApiDTO<>();
            body.setStatus("500");
            body.setMessage("请填写必填项目");
            if (callback != null) {
                callback.onResponse(null, Response.success(body));
            }
            return null;
        } else {
            return result;
        }
    }
}
