package com.doctor.sun.entity;

/**
 * Created by rick on 12/22/15.
 */
public class ItemTextInput extends BaseItem {


    private String title;
    private String input;

    public ItemTextInput(int itemLayoutId, String title) {
        super(itemLayoutId);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

}
