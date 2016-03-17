package io.ganguo.library.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用View适配器
 * <p/>
 * Created by Tony on 10/21/14.
 */
public class CommonAdapter extends BaseAdapter implements IListAdapter<View> {

    private List<View> mViews;

    public CommonAdapter() {

    }

    public CommonAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews == null ? 0 : mViews.size();
    }

    @Override
    public View getItem(int position) {
        return mViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mViews.get(position);
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
