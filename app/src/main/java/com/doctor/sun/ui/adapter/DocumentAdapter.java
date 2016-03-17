package com.doctor.sun.ui.adapter;

import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PItemDocumentBinding;
import com.doctor.sun.ui.activity.patient.DocumentActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by lucas on 1/7/16.
 */
public class DocumentAdapter extends SimpleAdapter {

    public interface GetEditMode {
        boolean getEditMode();
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.p_item_document;
    }

    public DocumentAdapter(DocumentActivity documentActivity) {
        super(documentActivity);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder vh, int position) {
        GetEditMode getEditMode = (GetEditMode) getContext();
        boolean editStatue = getEditMode.getEditMode();
        if(vh.getItemViewType() == R.layout.p_item_document){
            PItemDocumentBinding binding = (PItemDocumentBinding)vh.getBinding();
            if(editStatue){
                binding.ivSelect.setVisibility(View.VISIBLE);
            }else{
                binding.ivSelect.setVisibility(View.GONE);
            }
        }
    }
}