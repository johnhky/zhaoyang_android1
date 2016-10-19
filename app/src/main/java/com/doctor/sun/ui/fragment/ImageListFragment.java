package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.pager.BindingPagerAdapter;
import com.doctor.sun.vo.ItemPickImage;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by rick on 14/10/2016.
 */

public class ImageListFragment extends TabActivity {
    public static final String TAG = ImageListFragment.class.getSimpleName();

    public static Intent intentFor(Context context, ArrayList<String> images) {
        Intent intent = new Intent(context, ImageListFragment.class);
        intent.putExtra(Constants.DATA, images);
        return intent;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        ArrayList<String> data = getIntent().getStringArrayListExtra(Constants.DATA);
        ArrayList<LayoutId> result = new ArrayList<>();
        if (data != null) {
            Collection<ItemPickImage> transform = Collections2.transform(data, new Function<String, ItemPickImage>() {
                @Override
                public ItemPickImage apply(String input) {
                    ItemPickImage itemPickImage = new ItemPickImage(R.layout.item_image_page, input);
                    itemPickImage.setItemId(input);
                    return itemPickImage;
                }
            });
            result.addAll(transform);
        }

        BindingPagerAdapter<LayoutId> layoutIdBindingPagerAdapter = new BindingPagerAdapter<>();
        layoutIdBindingPagerAdapter.setItems(result);
        return layoutIdBindingPagerAdapter;
    }

    @Override
    protected void initPagerTabs() {
        binding.pagerTabsContainer.setVisibility(View.GONE);
    }
}
