package io.ganguo.library.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用View PagerAdapter
 * <p/>
 * Created by Tony on 10/20/14.
 */
public class CommonPageAdapter extends PagerAdapter implements IListAdapter<View> {
    private static final String TAG = CommonPageAdapter.class.getName();
    private List<View> mViews;

    public CommonPageAdapter() {

    }

    public CommonPageAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews == null ? 0 : mViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        try {
            container.addView(view);
        } catch (Exception e) {
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setList(List<View> views) {
        mViews = views;
        notifyDataSetChanged();
    }

    @Override
    public List<View> getList() {
        return mViews;
    }

    @Override
    public void addAll(List<View> list) {
        if (mViews == null) {
            mViews = new ArrayList<>(list.size());
        }
        mViews.addAll(list);
    }

    @Override
    public void add(View item) {
        if (mViews == null) {
            mViews = new ArrayList<>();
        }
        mViews.add(item);
    }

    @Override
    public View getItem(int position) {
        return mViews.get(position);
    }

    @Override
    public boolean contains(View item) {
        return mViews == null ? false : mViews.contains(item);
    }

    @Override
    public void remove(View item) {
        if (mViews != null) {
            mViews.remove(item);
        }
    }

    @Override
    public void remove(int position) {
        if (mViews != null) {
            mViews.remove(position);
        }
    }

    @Override
    public void clear() {
        if (mViews != null) {
            mViews.clear();
        }
    }

}
