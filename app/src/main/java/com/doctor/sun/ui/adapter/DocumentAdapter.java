package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.R;

/**
 * Created by lucas on 1/7/16.
 */
public class DocumentAdapter extends MultiSelectAdapter {

    public interface GetEditMode {
        boolean getEditMode();
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.p_item_document;
    }

    public DocumentAdapter(Context context){
        super(context, null);
    }

    public boolean isEditMode() {
        GetEditMode getEditMode = (GetEditMode) getContext();
        return getEditMode.getEditMode();
    }
}