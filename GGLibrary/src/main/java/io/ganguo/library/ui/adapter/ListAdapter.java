package io.ganguo.library.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.R;

/**
 * 通用列表适配器
 * <p/>
 * Created by Tony on 4/2/15.
 */
public abstract class ListAdapter<T> extends android.widget.BaseAdapter implements IViewCreator<T>, IListAdapter<T> {

    private Context mContext;
    private List<T> mDataList = null;

    protected ListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList == null ? null : mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = createView(getContext(), position, getItem(position));
            convertView = viewHolder.getView();
        } else {
            viewHolder = (ViewHolder) convertView.getTag(R.string.tag_view_holder);
        }
        viewHolder.putData(ViewHolder.POSITION, position);
        viewHolder.putData(ViewHolder.ITEM, getItem(position));

        updateView(viewHolder, position, getItem(position));
        return convertView;
    }

    @Override
    public void setList(List<T> list) {
        mDataList = list;
    }

    @Override
    public List<T> getList() {
        return mDataList;
    }

    @Override
    public void addAll(List<T> list) {
        if (mDataList == null) {
            mDataList = new ArrayList<>(list.size());
        }
        mDataList.addAll(list);

    }

    @Override
    public void add(T item) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(item);
    }

    @Override
    public boolean contains(T item) {
        if (mDataList != null) {
            return mDataList.contains(item);
        }
        return false;
    }

    @Override
    public void remove(T item) {
        if (mDataList != null) {
            mDataList.remove(item);
        }
    }

    @Override
    public void remove(int position) {
        if (mDataList != null) {
            mDataList.remove(position);
        }
    }

    @Override
    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

}
