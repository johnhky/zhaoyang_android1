package com.doctor.sun.vo;

import android.support.v7.widget.ListPopupWindow;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rick on 28/10/2016.
 */

public class ItemAutoCompleteTextInput<T> extends ItemTextInput2 {
    public static final String TAG = ItemAutoCompleteTextInput.class.getSimpleName();
    private long lastFilterTime = 0;
    private ArrayList<T> allEntries = new ArrayList<>();
    private ArrayList<T> entries = new ArrayList<>();
    private AdapterView.OnItemClickListener listener;
    private Predicate<T> predicate;
    private ListPopupWindow popupWindow;
    private ArrayAdapter<T> arrayAdapter;
    private boolean dismissByUser = false;

    public ItemAutoCompleteTextInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
    }

    public void showDropDown(View view, Editable e) {
        if (dismissByUser) {
            dismissByUser = false;
            return;
        }
        if (popupWindow == null) {
            popupWindow = new ListPopupWindow(view.getContext());
            popupWindow.setAnchorView(view);
            popupWindow.setOnItemClickListener(listener);
            popupWindow.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        }

        arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, getEntries());
        popupWindow.setAdapter(arrayAdapter);

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (!entries.isEmpty()) {
            popupWindow.show();
        }
    }

    private ArrayList<T> getEntries() {
        if (System.currentTimeMillis() - lastFilterTime > 250 && !allEntries.isEmpty()) {
            Collection<T> filter = Collections2.filter(allEntries, predicate);
            entries.clear();
            entries.addAll(filter);
            lastFilterTime = System.currentTimeMillis();
        }
        return entries;
    }

    public ArrayList<T> getFilteredEntries() {
        return entries;
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


    public void setAllEntries(List<T> allEntries) {
        this.allEntries.addAll(allEntries);
    }

    public void setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }
}
