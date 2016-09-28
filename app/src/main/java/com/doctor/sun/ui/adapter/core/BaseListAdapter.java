package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

/**
 * Created by rick on 13/8/2016.
 */
public abstract class BaseListAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> implements AdapterOps<SortedItem> {
    public static final String TAG = BaseListAdapter.class.getSimpleName();

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final SparseBooleanArray mConfig = new SparseBooleanArray();
    private final SparseArray<String> mStringConfig = new SparseArray<>();
    private final SparseIntArray mIntConfig = new SparseIntArray();

    private LayoutIdInterceptor idInterceptor = new DefaultLayoutIdInterceptor();

    BaseListAdapter(Context context) {
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
//        try {
//            B binding = DataBindingUtil.inflate(getInflater(), viewType, parent, false);
//            return new BaseViewHolder<>(binding);
//        } catch (InflateException e) {
//            Log.e(TAG, "viewType" + viewType);
//            B inflate = DataBindingUtil.inflate(getInflater(), R.layout.item_empty, parent, false);
//            return new BaseViewHolder<>(inflate);
//        }
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
