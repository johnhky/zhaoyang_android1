package com.doctor.sun.vm;

import android.databinding.Observable;

import java.util.ArrayList;

/**
 * Created by rick on 8/2/2017.
 */
public class MgPerUnitInput extends ItemTextInput2 {

    private ItemRadioDialog dialog = new ItemRadioDialog(0);

    public MgPerUnitInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
        dialog.setTitle("剂量单位");
        dialog.setSelectedItem(0);
        dialog.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (dialog.getSelectedItem() == 2) {
                    setResult("");
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }
            }
        });
    }

    @Override
    public void setSubTitle(String subTitle) {
        int selectedItem = dialog.getSelectedItem();
        dialog.clearOptions();
        ArrayList<String> options = new ArrayList<>();
        options.add("毫克/" + subTitle);
        options.add("克/" + subTitle);
        options.add("1s(不详)");
        dialog.addOptions(options);
        dialog.setSelectedItem(selectedItem);
        super.setSubTitle(subTitle);
    }

    @Override
    public String getValue() {
        switch (dialog.getSelectedItem()) {
            case 0:
                return super.getValue() + ",毫克";
            case 1:
                return super.getValue() + ",克";
            case 2:
                return "-1" + ",ignored";
        }
        return "";
    }

    public ItemRadioDialog getDialog() {
        return dialog;
    }
}
