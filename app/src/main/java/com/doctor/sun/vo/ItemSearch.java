package com.doctor.sun.vo;

import android.databinding.Bindable;
import android.widget.EditText;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.HashMap;

import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 27/10/2016.
 */

public class ItemSearch extends BaseItem {

    private int submit;
    private String keyword = "";


    @Bindable
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        notifyPropertyChanged(BR.keyword);
    }

    public void editKeyword(final EditText editText) {
        setUserSelected(true);
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                Systems.showKeyboard(editText);
            }
        }, 500);
    }

    @Bindable
    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
        notifyPropertyChanged(BR.submit);
    }

    public boolean incrementSubmit() {
        setSubmit(getSubmit() + 1);
        return true;
    }

    @Override
    public long getCreated() {
        return Long.MIN_VALUE;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        return null;
    }
}
