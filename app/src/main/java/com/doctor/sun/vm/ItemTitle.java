package com.doctor.sun.vm;

/**
 * Created by kb on 14/12/2016.
 */

public class ItemTitle extends BaseItem {

    private int layoutId;
    private String title;
    private String subtitle;
    public boolean hasSubtitle;

    public ItemTitle(int layoutId, String title) {
        this.layoutId = layoutId;
        this.title = title;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        hasSubtitle = true;
    }
}
