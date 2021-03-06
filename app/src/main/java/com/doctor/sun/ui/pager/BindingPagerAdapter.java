package com.doctor.sun.ui.pager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.vm.LayoutId;

import java.util.List;


/**
 */
public class BindingPagerAdapter<T extends LayoutId> extends PagerAdapter {
    private List<T> items;
    private SparseArray<View> mScrapList = new SparseArray<>();


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mScrapList.get(position);
        if (view == null) {
            view = onCreateView(container, position);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mScrapList.put(position, (View) object);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    protected LayoutId getItem(int position) {
        return items.get(position);
    }

    protected View onCreateView(ViewGroup parent, int position) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        LayoutId item = getItem(position);
        ViewDataBinding binding = DataBindingUtil.inflate(from, item.getItemLayoutId(), parent, false);
        binding.setVariable(BR.data, item);
        return binding.getRoot();
    }
}
