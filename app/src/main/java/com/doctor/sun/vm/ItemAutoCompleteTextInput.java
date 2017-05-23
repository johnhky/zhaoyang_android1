package com.doctor.sun.vm;

import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.doctor.sun.entity.DrugDetail;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DrugModule;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rick on 28/10/2016.
 */

public class ItemAutoCompleteTextInput<T> extends ItemTextInput2 {
    public static final String TAG = ItemAutoCompleteTextInput.class.getSimpleName();
    private AdapterView.OnItemClickListener listener;
    private ListPopupWindow popupWindow;
    private boolean dismissByUser = false;

    public ItemAutoCompleteTextInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }

    public void showDropDown(View view) {
        if (dismissByUser) {
            dismissByUser = false;
            return;
        }
        initPopupWindow(view);

        ArrayAdapter<DrugDetail> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, getEntries());
        popupWindow.setAdapter(arrayAdapter);

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow.show();
    }

    private void initPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(view.getContext());
            popupWindow.setAnchorView(view);
            popupWindow.setOnItemClickListener(listener);
            popupWindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        }
    }

    public List<DrugDetail> getEntries() {
        RealmResults<DrugDetail> drug_name = Realm.getDefaultInstance()
                .where(DrugDetail.class)
                .contains("drug_name", getResult())
                .findAll().distinct("drug_name");
        return drug_name.subList(0, drug_name.size());
    }

    @NonNull
    public List<DrugDetail> getFilteredEntries() {
        return getEntries();
    }

    public AdapterView.OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
        if (popupWindow != null) {
            popupWindow.setOnItemClickListener(this.listener);
        }
    }


    public void dismissDialog() {
        popupWindow.dismiss();
        dismissByUser = true;
    }
}
