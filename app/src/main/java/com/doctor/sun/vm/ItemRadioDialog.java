package com.doctor.sun.vm;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BR;
import com.doctor.sun.util.AddressTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

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
            return getTitle();
        }
        return options.get(selectedItem);
    }


    public void showPopupWindow(View view, Context context) {
        ArrayList<String> items = options;
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items));
        listPopupWindow.setWidth(view.getWidth());
        listPopupWindow.setHeight(450);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                listPopupWindow.dismiss();
                notifyChange();
            }
        });
        listPopupWindow.setAnchorView(view);
        listPopupWindow.show();
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

    public void addOptions(List<String> options) {
        this.options.addAll(options);
    }

    public void addOptions(String[] stringArray) {
        Collections.addAll(options, stringArray);
    }

    public boolean hasSameOptions(List<String> strings) {
        return options.equals(strings);
    }

    public boolean hasSameOptions(String[] strings) {
        return options.equals(Arrays.asList(strings));
    }

    public void clearOptions() {
        options.clear();
        selectedItem = -1;
    }

    public void removeOption(int option) {
        options.remove(option);
    }

    public ArrayList<String> getOptions() {
        return options;
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
