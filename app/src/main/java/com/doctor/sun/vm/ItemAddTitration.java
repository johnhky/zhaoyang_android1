package com.doctor.sun.vm;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heky on 17/4/18.
 */

public class ItemAddTitration extends BaseItem {
    private String content;
    public int itemSize = 1;
    public int pCount = 1;
    private String subTitle;
    public ItemAddTitration(int layoutId){
        super(layoutId);
    }
    @NonNull
    public void addTitration(SortedListAdapter adapter){
        TitrationTextInput titrationTextInput = createAdapter();
        adapter.insert(titrationTextInput);
        notifyChange();
    }
    public TitrationTextInput createAdapter(){
        TitrationTextInput titrationTextInput = new TitrationTextInput(R.layout.item_titration_tab,"","");
        titrationTextInput.setSubTitle(subTitle);
        if(pCount==1){
            titrationTextInput.setResult("1");
            titrationTextInput.setChecked(true);
        }else{

        }
        pCount+=1;
        itemSize+=1;
        titrationTextInput.setItemId(getPosition()+pCount+"");
        titrationTextInput.setPosition(itemSize());
        registerCallBack(titrationTextInput);
        return titrationTextInput;
    }

    public void registerCallBack(final TitrationTextInput titrationTextInput){
        titrationTextInput.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId== BR.removed){
                    itemSize-=1;
                    pCount-=1;
                    notifyChange();
                }
            }
        });
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }


    @Bindable
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        notifyPropertyChanged(BR.subTitle);
    }
    public int itemSize(){
        return itemSize-1;
    }
}
