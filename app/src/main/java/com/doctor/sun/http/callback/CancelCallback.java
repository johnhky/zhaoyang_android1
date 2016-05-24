package com.doctor.sun.http.callback;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 22/1/2016.
 */
public class CancelCallback extends ApiCallback<String> {
    private final BaseViewHolder vh;
    private final BaseAdapter adapter;

    public CancelCallback(BaseViewHolder vh, BaseAdapter adapter) {
        this.vh = vh;
        this.adapter = adapter;
    }

    @Override
    protected void handleResponse(String response) {
        int adapterPosition = vh.getAdapterPosition();
        adapter.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
    }

    @Override
    protected void handleApi(ApiDTO<String> body) {
        int adapterPosition = vh.getAdapterPosition();
        adapter.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
    }
}
