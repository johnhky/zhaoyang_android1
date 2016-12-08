package com.doctor.sun.vm;

import android.view.Gravity;
import android.widget.AdapterView;

/**
 * Created by rick on 21/3/2016.
 */
public class SpinnerViewModel {
    private int titleGravity = Gravity.END | Gravity.CENTER_VERTICAL;
    private String title;
    private AdapterView.OnItemSelectedListener listener;
    private boolean isLarge;

    public SpinnerViewModel(String title, AdapterView.OnItemSelectedListener listener) {
        this.title = title;
        this.listener = listener;
    }

    public int getTitleGravity() {
        return titleGravity;
    }

    public void setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdapterView.OnItemSelectedListener getListener() {
        return listener;
    }

    public void setListener(AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public boolean isLarge() {
        return isLarge;
    }

    public void setLarge(boolean large) {
        isLarge = large;
    }
}
