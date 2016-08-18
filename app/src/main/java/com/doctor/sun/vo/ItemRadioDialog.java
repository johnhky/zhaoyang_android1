package com.doctor.sun.vo;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rick on 23/12/2015.
 */
public class ItemRadioDialog extends BaseItem implements LayoutId {

    private int selectedItem = 0;
    private String title = "";
    private ArrayList<String> options = new ArrayList<>();

    public ItemRadioDialog(int layoutId) {
        super(layoutId);
    }


    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyPropertyChanged(BR.selectedItem);
    }

    public String getSelectedItemText() {
        return options.get(selectedItem);
    }

    public void showOptions(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.items(options)
                .title(title)
                .itemsCallbackSingleChoice(selectedItem, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedItem = which;
                        notifyChange();
                        return false;
                    }
                }).show();
    }


    public void addOptions(ArrayList<String> options) {
        this.options.addAll(options);
    }

    public void addOptions(String[] stringArray) {
        Collections.addAll(options, stringArray);
    }

    @Override
    public String getValue() {
        return String.valueOf(selectedItem + 1);
    }
}
