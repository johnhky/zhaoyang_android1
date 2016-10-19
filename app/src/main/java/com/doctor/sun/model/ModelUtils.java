package com.doctor.sun.model;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kb on 16-9-18.
 */

final class ModelUtils {

    static void insertDividerMarginLR(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_start13_end13);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertDividerNoMargin(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    static void insertSpace(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_30dp);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    static HashMap<String, String> toHashMap(SortedListAdapter adapter, Callback callback) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                ApiDTO<String> body = new ApiDTO<>();
                body.setStatus("500");
                body.setMessage(item.errorMsg());
                callback.onResponse(null, Response.success(body));
                return null;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }
}
