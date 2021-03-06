package com.doctor.sun.vm;

import android.view.View;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.doctor.sun.Settings;
import com.doctor.sun.entity.Doctor;

/**
 * Created by rick on 23/12/2015.
 */
public class ItemRadioGroup extends BaseItem implements LayoutId {
    private int layoutId;
    private int selectedItem = -1;
    private OnCheckedChangeListener listener;

    public ItemRadioGroup(int layoutId) {
        super(layoutId);
        this.layoutId = layoutId;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }


    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        if (listener != null) {
            listener.onCheckedChanged(null, selectedItem);
        }
        notifyChange();
    }

    public void select(View view) {
        int selectedItem = Integer.parseInt(view.getTag().toString());
        if (listener != null) {
            listener.onCheckedChanged(null, selectedItem);
        }
        setSelectedItem(selectedItem);
    }

    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public OnCheckedChangeListener getListener() {
        return listener;
    }

    @Override
    public String getValue() {
        if (selectedItem == -1) {
            return "";
        }
        return String.valueOf(selectedItem);
    }

    public boolean isShow(){
        Doctor doctor = Settings.getDoctorProfile();
        if (doctor.getSpecialistCateg()==1||doctor.getIsOpen().isSimple()==false){
            return false;
        }else{
            return true;
        }
    }


}