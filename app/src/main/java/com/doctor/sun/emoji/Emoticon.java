package com.doctor.sun.emoji;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

/**
 * Created by rick on 7/4/2016.
 */
public class Emoticon implements LayoutId {
    public final int itemLayoutId;
    private String id;
    private String tag;
    private String assetPath;
    private int drawableId;

    public Emoticon(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public Emoticon() {
        this.itemLayoutId = R.layout.item_emoji;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    @Override
    public String toString() {
        return "Emoticon{" +
                "id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", assetPath='" + assetPath + '\'' +
                '}';
    }

    public void onSelect(View view) {
        Activity mActivity = (Activity) view.getContext();
        View focusCurrent = mActivity.getWindow().getCurrentFocus();
        if (focusCurrent == null || !(focusCurrent instanceof EditText)) return;
        TextView textView = (TextView) focusCurrent;

        EmoticonManager.insertEmoticon(mActivity, textView, tag);
    }

    public void onDelete(View view) {
        Activity mActivity = (Activity) view.getContext();
        View focusCurrent = mActivity.getWindow().getCurrentFocus();
        if (focusCurrent == null || !(focusCurrent instanceof EditText)) return;
        EditText textView = (EditText) focusCurrent;

        textView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }
}
