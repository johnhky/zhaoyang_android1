package com.doctor.sun.ui.model;

import android.databinding.BaseObservable;
import android.view.View;

import com.doctor.sun.R;


/**
 * Created by rick on 10/15/15.
 */
public class HeaderViewModel extends BaseObservable {
    private HeaderView mView;
    private int lineBackground = 0;
    private int leftIcon = R.drawable.ic_back;
    private int rightIcon = 0;
    private int rightFirstIcon = 0;
    private int midIcon = 0;
    private String leftTitle = "";
    private String rightTitle = "";
    private String rightFirstTitle = "";
    private String midTitle = "";

    public HeaderViewModel(HeaderView mView) {
        this.mView = mView;
    }

    public void onBackClicked(View view) {
        mView.onBackClicked();
    }

    public void onTitleClicked(View view) {
        mView.onTitleClicked();
    }

    public void onMenuClicked(View view) {
        mView.onMenuClicked();
    }
    public void onFirstMenuClicked(View view) {
        mView.onFirstMenuClicked();
    }

    public int getLineBackground() {
        return lineBackground;
    }

    public HeaderViewModel setLineBackground(int lineBackground) {
        this.lineBackground = lineBackground;
        return this;
    }

    public int getLeftIcon() {
        return leftIcon;
    }

    public HeaderViewModel setLeftIcon(int leftIcon) {
        this.leftIcon = leftIcon;
        return this;
    }

    public int getRightIcon() {
        return rightIcon;
    }

    public HeaderViewModel setRightIcon(int rightIcon) {
        this.rightIcon = rightIcon;
        return this;
    }

    public int getMidIcon() {
        return midIcon;
    }

    public HeaderViewModel setMidIcon(int midIcon) {
        this.midIcon = midIcon;
        return this;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public HeaderViewModel setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
        return this;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public HeaderViewModel setRightTitle(String rightTitle) {
        this.rightTitle = rightTitle;
        notifyChange();
        return this;
    }

    public String getMidTitle() {
        return midTitle;
    }

    public HeaderViewModel setMidTitle(String midTitle) {
        this.midTitle = midTitle;
        return this;
    }

    public HeaderViewModel setRightFirstIcon(int rightFirstIcon) {
        this.rightFirstIcon = rightFirstIcon;
        return this;
    }

    public HeaderViewModel setRightFirstTitle(String rightFirstTitle) {
        this.rightFirstTitle = rightFirstTitle;
        return this;
    }

    public int getRightFirstIcon() {
        return rightFirstIcon;
    }

    public String getRightFirstTitle() {
        return rightFirstTitle;
    }

    public boolean leftVisible() {
        return getLeftIcon() != 0 || !getLeftTitle().equals("");
    }
    public boolean midVisible() {
        return getMidIcon() != 0 || !getMidTitle().equals("");
    }
    public boolean  rightVisible() {
        return getRightIcon() != 0 || !getRightTitle().equals("");
    }
    public boolean  rightFirstVisible() {
        return getRightFirstIcon() != 0 || !getRightFirstTitle().equals("");
    }

    public interface HeaderView {
        void onBackClicked();

        void onTitleClicked();

        void onMenuClicked();

        void onFirstMenuClicked();
    }
}
