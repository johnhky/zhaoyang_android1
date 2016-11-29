package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by rick on 13/8/2016.
 */
public abstract class BaseListAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements AdapterOps<T> {
    public static final String TAG = BaseListAdapter.class.getSimpleName();

    private final SparseBooleanArray mBooleanConfig = new SparseBooleanArray();
    private final SparseArray<String> mStringConfig = new SparseArray<>();
    private final SparseIntArray mIntConfig = new SparseIntArray();
    private final SparseArray<Long> mLongConfig = new SparseArray<>();

    private LayoutIdInterceptor idInterceptor = new DefaultLayoutIdInterceptor();

    BaseListAdapter() {
    }

    protected LayoutInflater getInflater(Context context) {
        return LayoutInflater.from(context);
    }

    LayoutIdInterceptor getIdInterceptor() {
        return idInterceptor;
    }

    @Override
    public BaseViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            B binding = DataBindingUtil.inflate(getInflater(parent.getContext()), viewType, parent, false);
            return new BaseViewHolder<>(binding);
        } catch (Exception e) {
            Log.e(TAG, "onCreateViewHolder: R.layout: " + Integer.toHexString(viewType));
            MobclickAgent.reportError(parent.getContext(), e.getCause());
            B inflate = DataBindingUtil.inflate(getInflater(parent.getContext()), com.doctor.sun.R.layout.item_error, parent, false);
            return new BaseViewHolder<>(inflate);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<B> holder, int position) {
        holder.getBinding().setVariable(BR.adapter, this);
        holder.bindTo(get(position));
    }

    public void setLayoutIdInterceptor(LayoutIdInterceptor idInterceptor) {
        if (idInterceptor == null) {
            this.idInterceptor = new DefaultLayoutIdInterceptor();
        } else {
            this.idInterceptor = idInterceptor;
        }
        notifyDataSetChanged();
    }


    public boolean getBoolean(int key) {
        return mBooleanConfig.get(key, false);
    }

    public void putBoolean(int key, boolean value) {
        mBooleanConfig.put(key, value);
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
        return mLongConfig.get(key, 0L);
    }


    public abstract T get(int position);


    public interface LayoutIdInterceptor {
        int intercept(int origin);
    }

    private class DefaultLayoutIdInterceptor implements LayoutIdInterceptor {
        @Override
        public int intercept(int origin) {
            return origin;
        }
    }

    @Deprecated
    public boolean isSelected(BaseViewHolder vh) {
        return false;
    }
}
