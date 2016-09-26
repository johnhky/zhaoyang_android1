package com.doctor.sun.ui.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

public class AlwaysFilterTextView extends AutoCompleteTextView {
    boolean firstTime = false;

    public AlwaysFilterTextView(Context context) {
        super(context);
    }

    public AlwaysFilterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysFilterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (firstTime) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryPerformFiltering();
                }
            }, 500L);
        } else {
            tryPerformFiltering();
        }
    }

    public void tryPerformFiltering() {
        try {
            performFiltering(getText(), KeyEvent.KEYCODE_UNKNOWN);
        } catch (NullPointerException ignored) {

        }
    }

}