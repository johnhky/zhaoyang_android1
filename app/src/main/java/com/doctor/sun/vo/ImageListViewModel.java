package com.doctor.sun.vo;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 1/8/2016.
 */

public class ImageListViewModel extends BaseItem {
    public ImageListViewModel(int itemLayoutId) {
        super(itemLayoutId);
    }

    private SimpleAdapter<PickImageViewModel, ViewDataBinding> simpleAdapter;

    public SimpleAdapter adapter(Context context) {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter<>(context);
            simpleAdapter.mapLayout(R.layout.item_pick_date, R.layout.item_reminder);
            simpleAdapter.onFinishLoadMore(true);
            simpleAdapter.add(new PickImageViewModel(R.layout.item_pick_image, ""));
        }
        return simpleAdapter;
    }
}
