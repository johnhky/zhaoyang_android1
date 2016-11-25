package com.doctor.sun.vo;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rick on 23/12/2015.
 */
public class ItemRadioDialog extends BaseItem implements LayoutId {

    private int selectedItem = -1;
    private ArrayList<String> options = new ArrayList<>();
    private Evaluator evaluator;

    public ItemRadioDialog(int layoutId) {
        super(layoutId);
        setAction("选择");
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
        if (selectedItem == -1) {
            return "请选择" + getTitle();
        }
        return options.get(selectedItem);
    }

    public void showOptions(Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.items(options)
                .title(getTitle())
                .itemsCallbackSingleChoice(selectedItem, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedItem = which;
                        notifyChange();
                        return false;
                    }
                }).show();
    }

    public void setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void addOptions(ArrayList<String> options) {
        this.options.addAll(options);
    }

    public void addOptions(String[] stringArray) {
        Collections.addAll(options, stringArray);
    }

    @Override
    public String getValue() {
        if (selectedItem == -1) {
            return "";
        }
        if (evaluator != null) {
            return evaluator.evaluate(this);
        }
        return String.valueOf(selectedItem + 1);
    }


    public interface Evaluator {
        String evaluate(ItemRadioDialog dialog);
    }

    public static class TextEvaluator implements Evaluator {

        @Override
        public String evaluate(ItemRadioDialog dialog) {
            return dialog.getSelectedItemText();
        }
    }
}
