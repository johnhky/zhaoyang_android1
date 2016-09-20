package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 10/9/2016.
 */

public class QuestionsButton extends BaseItem {


    private boolean checked = false;
    @JsonProperty("open_title")
    public String openTitle;
    @JsonProperty("close_title")
    public String closeTitle;

    @JsonProperty("questions")
    public List<Questions2> questions;
    private List<SortedItem> datas;

    public void setDatas(List<SortedItem> datas) {
        this.datas = datas;
    }

    public List<SortedItem> getDatas() {
        return datas;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_question_toggle;
    }

    public boolean isChecked() {
        return checked;
    }


    public void toggleChecked(SortedListAdapter adapter) {
        if (checked) {
            checked = false;
            notifyChange();
            for (SortedItem data : datas) {
                adapter.removeItem(data);
            }
        } else {
            checked = true;
            adapter.insertAll(datas);
        }
        notifyChange();
    }

    @Override
    public String getTitle() {
        if (isChecked()) {
            return openTitle;
        } else {
            return closeTitle;
        }
    }
}
