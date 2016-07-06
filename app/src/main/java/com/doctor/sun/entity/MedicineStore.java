package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.R;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by lucas on 1/29/16.
 * 寄药小助手item,只是负责跳转页面,
 * TODO 写个跳转页面的item替换掉
 */
public class MedicineStore implements LayoutId, SortedItem {
    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_medicine_store;
    }

    public long unReadMsgCount() {
        RealmQuery<TextMsg> q = Realm.getDefaultInstance().where(TextMsg.class)
                .equalTo("sessionId", MedicineStoreActivity.ADMIN_DRUG);
        return q.equalTo("haveRead", false).count();
    }

    public void medicineStore(Context context, int count) {
        Intent intent = MedicineStoreActivity.makeIntent(context, count);

        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return getItemLayoutId();
    }

    @Override
    public long getCreated() {
        return Long.MAX_VALUE - 1;
    }

    @Override
    public String getKey() {
        return "MEDICINE_STORE";
    }
}
