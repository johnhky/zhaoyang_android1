package com.doctor.sun.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * This class overrides the onKeyPreIme method to dispatch a key event if the
 * KeyEvent passed to onKeyPreIme has a key code of KeyEvent.KEYCODE_BACK.
 * This allows key event listeners to detect that the soft keyboard was
 * dismissed.
 */
public class ExtendedEditText extends EditText {

    private KeyboardDismissListener listener;

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ExtendedEditText(Context context) {
        super(context);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            listener = (KeyboardDismissListener) getContext();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Activity must implement KeyboardDismissListener");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listener = null;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            dispatchKeyEvent(event);
            if (listener != null) {
                listener.onKeyboardDismiss();
            }
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface KeyboardDismissListener {
        void onKeyboardDismiss();
    }
}