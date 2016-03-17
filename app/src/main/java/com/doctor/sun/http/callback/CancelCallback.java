package com.doctor.sun.http.callback;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 22/1/2016.
 */
public class CancelCallback extends ApiCallback<String> {
    private final BaseViewHolder vh;
    private final BaseAdapter component;

    public CancelCallback(BaseViewHolder vh, BaseAdapter component) {
        this.vh = vh;
        this.component = component;
    }

    @Override
    protected void handleResponse(String response) {
        int adapterPosition = vh.getAdapterPosition();
        component.remove(adapterPosition);
        component.notifyItemRemoved(adapterPosition);
    }

    @Override
    protected void handleApi(ApiDTO<String> body) {
        int adapterPosition = vh.getAdapterPosition();
        component.remove(adapterPosition);
        component.notifyItemRemoved(adapterPosition);
    }
}
