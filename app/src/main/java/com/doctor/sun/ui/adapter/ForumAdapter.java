package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemForumsBinding;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.handler.QCategoryHandler;

/**
 * Created by Lynn on 12/24/15.
 */
public class ForumAdapter extends SimpleAdapter<QCategoryHandler, ItemForumsBinding> {

    public ForumAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder vh, final int position) {
        super.onBindViewBinding(vh, position);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_forums;
    }
}
