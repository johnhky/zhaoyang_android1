package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by rick on 22/6/2016.
 */
public class SortedListAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<BaseViewHolder<B>> {
    final Context mContext;
    final LayoutInflater mInflater;
    final SortedList<SortedItem> mList;
    final Map<String, SortedItem> mUniqueMapping = new HashMap<>();

    public SortedListAdapter(Context context, SortedList<SortedItem> mList) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    public SortedListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = new SortedList<>(SortedItem.class, new SortedListAdapterCallback<SortedItem>(this) {
            @Override
            public int compare(SortedItem o1, SortedItem o2) {
                if (o1.getCreated() < o2.getCreated()) {
                    return 1;
                }
                if (o1.getCreated() == o2.getCreated()) {
                    return 0;
                }
                return -1;
            }

            @Override
            public boolean areContentsTheSame(SortedItem oldItem, SortedItem newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(SortedItem item1, SortedItem item2) {
                String a = item1.getKey();
                String b = item2.getKey();
                return (a == null) ? (b == null) : a.equals(b);
            }
        });
    }

    protected LayoutInflater getInflater() {
        return mInflater;
    }

    protected Context getContext() {
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
        holder.bindTo(mList.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getLayoutId();
    }

    @Override
    final public int getItemCount() {
        return mList.size();
    }

    public void insert(SortedItem item) {
        String key = item.getKey();
        SortedItem existing = mUniqueMapping.put(key, item);
        if (existing == null) {
            mList.add(item);
        } else {
            int pos = mList.indexOf(existing);
            if (pos >= 0)
                mList.updateItemAt(pos, item);
        }
    }

    public void update(SortedItem item) {
        String key = item.getKey();
        SortedItem existing = mUniqueMapping.get(key);
        if (existing == null) {
            return;
        }
        int pos = mList.indexOf(existing);
        mUniqueMapping.put(key, item);
        if (pos >= 0)
            mList.updateItemAt(pos, item);
    }

    public void insertAll(List<SortedItem> items) {
        for (SortedItem item : items) {
            insert(item);
        }
    }

    public void swapList(List<SortedItem> items) {
        Set<String> newListKeys = new HashSet<>();
        for (SortedItem item : items) {
            newListKeys.add(item.getKey());
        }
        for (int i = mList.size() - 1; i >= 0; i--) {
            SortedItem item = mList.get(i);
            String key = item.getKey();
            if (!newListKeys.contains(key)) {
                mUniqueMapping.remove(key);
                mList.removeItemAt(i);
            }
        }
        insertAll(items);
    }

    public long getReferenceTimestamp() {
        int size = mList.size();
        if (size == 0) {
            return 0;
        }
        return mList.get(0).getCreated();
    }

    public void remove(SortedItem item) {
        SortedItem model = mUniqueMapping.remove(item.getKey());
        if (model != null) {
            boolean remove = mList.remove(item);
            if (!remove) {
                mUniqueMapping.put(item.getKey(), item);
            }
        }
    }

    public void clear() {
        mList.clear();
        mUniqueMapping.clear();
    }

    public int size() {
        return mList.size();
    }

    public int indexOf(SortedItem sortedItem) {
        return mList.indexOf(sortedItem);
    }

    public SortedItem get(int position) {
        return mList.get(position);
    }

    public SortedItem get(String key) {
        return mUniqueMapping.get(key);
    }

    public int inBetweenItemCount(int adapterPosition, String itemId) {
        SortedItem sortedItem = get(itemId);
        if (sortedItem == null) {
            return 0;
        }
        int parentPosition = indexOf(sortedItem);
        return adapterPosition - parentPosition;
    }
}
