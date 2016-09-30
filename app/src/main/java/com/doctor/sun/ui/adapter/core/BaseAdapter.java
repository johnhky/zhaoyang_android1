package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

import java.util.List;

/**
 * Created by rick on 10/20/15.
 */
public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements List<T>, AdapterOps<T> {

    private Context mContext;
    private LayoutInflater mInflater;

    private final SparseBooleanArray mConfig = new SparseBooleanArray();
    private final SparseArray<String> mStringConfig = new SparseArray<>();
    private final SparseIntArray mIntConfig = new SparseIntArray();
    private final SparseArray<Long> mLongConfig = new SparseArray<>();

    public BaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(getInflater(), viewType, parent, false);
        BaseViewHolder<B> vh = new BaseViewHolder<>(binding);
        return vh;
    }

    public abstract void onBindViewBinding(BaseViewHolder<B> vh, int position);


    @Override
    public void onBindViewHolder(BaseViewHolder<B> holder, int position) {
        onBindViewBinding(holder, position);
        holder.getBinding().setVariable(BR.adapter, this);
        holder.bindTo(get(position));
    }


    @Override
    public int getItemCount() {
        return size();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    public final String getStringRes(int resId) {
        return getContext().getResources().getString(resId);
    }

    public final String getStringRes(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemLayoutId(position);
    }

    protected abstract int getItemLayoutId(int position);

    public abstract View.OnClickListener onItemClick(final BaseAdapter adapter, final BaseViewHolder vh);

    public void select(BaseViewHolder vh, BaseAdapter adapter) {

    }

    public void select(BaseViewHolder vh, BaseAdapter adapter, boolean hasFocus) {

    }

    public boolean isSelected(BaseViewHolder vh) {
        return false;
    }

    public boolean getConfig(int key) {
        return mConfig.get(key, false);
    }

    public void setConfig(int key, boolean value) {
        mConfig.put(key, value);
    }

    public String getString(int key) {
        return mStringConfig.get(key, "");
    }

    public void putString(int key, String value) {
        mStringConfig.put(key, value);
    }

    public void putInt(int key, int value) {
        mIntConfig.put(key, value);
    }

    public int getInt(int key) {
        return mIntConfig.get(key, 0);
    }

    public void putLong(int key, long value) {
        mLongConfig.put(key, value);
    }

    public long getLong(int key) {
        return mLongConfig.get(key);
    }

}
