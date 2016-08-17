package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

/**
 * Created by rick on 13/8/2016.
 */
public abstract class BaseListAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements AdapterOps<SortedItem>{
    protected final Context mContext;
    protected final LayoutInflater mInflater;
    private final SparseBooleanArray mConfig = new SparseBooleanArray();
    protected LayoutIdInterceptor idInterceptor = new DefaultLayoutIdInterceptor();
    public BaseListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    protected LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    final public BaseViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(getInflater(), viewType, parent, false);
        return new BaseViewHolder<>(binding);
    }

    @Override
    final public void onBindViewHolder(BaseViewHolder<B> holder, int position) {
        holder.getBinding().setVariable(BR.adapter, this);
        holder.bindTo(get(position));
    }

    public void setLayoutIdInterceptor(@NonNull LayoutIdInterceptor idInterceptor) {
        this.idInterceptor = idInterceptor;
        notifyDataSetChanged();
    }

    public boolean getConfig(int key) {
        return mConfig.get(key, false);
    }

    public void setConfig(int key, boolean value) {
        mConfig.put(key, value);
    }

    @Override
    public int getItemViewType(int position) {
        int layoutId = get(position).getLayoutId();
        return idInterceptor.intercept(layoutId);
    }

    public abstract SortedItem get(int position);

    public interface LayoutIdInterceptor {
        int intercept(int origin);
    }

    public class DefaultLayoutIdInterceptor implements LayoutIdInterceptor {
        @Override
        public int intercept(int origin) {
            return origin;
        }
    }
}
