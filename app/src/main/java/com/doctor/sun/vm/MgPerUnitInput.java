package com.doctor.sun.vm;

import android.databinding.Bindable;

import com.doctor.sun.BR;
import com.google.common.base.Strings;

import java.util.ArrayList;

/**
 * Created by rick on 8/2/2017.
 */
public class MgPerUnitInput extends ItemTextInput2 {

    private boolean isUnKnowUnit = false;
    private ItemRadioDialog dialog = new ItemRadioDialog(0);

    public MgPerUnitInput(int itemLayoutId, String title, String hint) {
        super(itemLayoutId, title, hint);
        dialog.setTitle("剂量单位");
    }


    @Bindable
    public boolean isUnKnowUnit() {
        return isUnKnowUnit;
    }

    public void setUnKnowUnit(boolean unKnowUnit) {
        isUnKnowUnit = unKnowUnit;
        if (isUnKnowUnit) {
            setResult("");
        }
        notifyPropertyChanged(BR.unKnowUnit);
    }

    @Override
    public void setResult(String result) {
        if (isUnKnowUnit && !Strings.isNullOrEmpty(result)) {
            setUnKnowUnit(false);
        }
        super.setResult(result);
    }

    @Override
    public void setSubTitle(String subTitle) {
        dialog.clearOptions();
        ArrayList<String> options = new ArrayList<>();
        options.add("毫克/" + subTitle);
        options.add("克/" + subTitle);
        options.add("1s(不详)");
        dialog.addOptions(options);
        super.setSubTitle(subTitle);
    }

    @Override
    public String getValue() {
        if (isUnKnowUnit) {
            return "-1";
        }
        return super.getValue();
    }

    public ItemRadioDialog getDialog() {
        return dialog;
    }
}
