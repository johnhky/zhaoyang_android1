package com.doctor.sun.http.callback;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;

/**
 * Created by rick on 22/1/2016.
 */
public class CancelCallback extends ApiCallback<String> {
    private final BaseViewHolder vh;
    private final BaseListAdapter adapter;

    public CancelCallback(BaseViewHolder vh, BaseListAdapter adapter) {
        this.vh = vh;
        this.adapter = adapter;
    }

    @Override
    protected void handleResponse(String response) {
        int adapterPosition = vh.getAdapterPosition();
        adapter.removeItem(adapter.get(adapterPosition));
        adapter.notifyItemRemoved(adapterPosition);
    }

    @Override
    protected void handleApi(ApiDTO<String> body) {
        int adapterPosition = vh.getAdapterPosition();
        adapter.removeItem(adapter.get(adapterPosition));
        adapter.notifyItemRemoved(adapterPosition);
    }
}
