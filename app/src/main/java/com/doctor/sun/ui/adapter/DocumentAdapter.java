package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PItemDocumentBinding;
import com.doctor.sun.ui.activity.patient.DocumentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

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