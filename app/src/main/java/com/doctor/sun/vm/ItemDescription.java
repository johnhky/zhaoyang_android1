package com.doctor.sun.vm;

import com.doctor.sun.R;

/**
 * Created by kb on 13/12/2016.
 */

public class ItemDescription extends BaseItem {

    private String mainContent;
    private String subContent;
    public boolean hasSubContent = false;

    public int getItemLayoutId() {
        return R.layout.item_description2;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
        hasSubContent = true;
    }
}
