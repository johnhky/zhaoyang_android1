package com.doctor.sun.ui.adapter.core;

import android.databinding.ViewDataBinding;
import android.support.v7.util.SortedList;
import android.support.v7.widget.util.SortedListAdapterCallback;

import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by rick on 22/6/2016.
 */
public class SortedListAdapter<B extends ViewDataBinding> extends BaseListAdapter<SortedItem, B> {
    public static final String TAG = SortedListAdapter.class.getSimpleName();

    private SortedList<SortedItem> mList;
    private final Map<String, SortedItem> mUniqueMapping = new HashMap<>();

    public SortedListAdapter(SortedList<SortedItem> mList) {
        this.mList = mList;
    }

    public SortedListAdapter() {
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

    @Override
    final public int getItemCount() {
        return mList.size();
    }

    @Override
    public void insert(int position, SortedItem item) {
        String key = item.getKey();
        SortedItem existing = mUniqueMapping.put(key, item);
        if (existing == null) {
            mList.add(item);
            //Log.e(TAG, "insert: newItem" + item);
        } else {
            int pos = mList.indexOf(existing);
            if (pos >= 0) {
                //Log.e(TAG, "insert: existingItem" + item);
                mList.updateItemAt(pos, item);
            }
        }
    }

    @Override
    public void insert(SortedItem item) {
        String key = item.getKey();
        SortedItem existing = mUniqueMapping.put(key, item);
        if (existing == null) {
            mList.add(item);
            //Log.e(TAG, "insert: newItem" + item);
        } else {
            int pos = mList.indexOf(existing);
            if (pos >= 0) {
                //Log.e(TAG, "insert: existingItem" + item);
                mList.updateItemAt(pos, item);
            }
        }
    }

    @Override
    public void update(SortedItem item) {
        String key = item.getKey();
        SortedItem existing = mUniqueMapping.get(key);
        if (existing == null) {
            //Log.e(TAG, "update: notFound" + item);
            return;
        }
        int pos = mList.indexOf(existing);
        mUniqueMapping.put(key, item);
        if (pos >= 0) {
            //Log.e(TAG, "update: existingItem" + item);
            mList.updateItemAt(pos, item);
        }
    }

    @Override
    public void update(int position, SortedItem item) {
        update(item);
    }

    @Override
    public void insertAll(List<SortedItem> items) {
//        Log.d(TAG, "insertAll() called with: items = [" + items + "]");
        for (SortedItem item : items) {
            insert(item);
        }
    }

    @Override
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

    @Override
    public void removeItem(SortedItem item) {
//        Log.d(TAG, "removeItem() called with: item = [" + item + "]");
        SortedItem model = mUniqueMapping.remove(item.getKey());
        if (model != null) {
            boolean remove = mList.remove(item);
            if (!remove) {
                mUniqueMapping.put(item.getKey(), item);
            }
        }
    }

    @Override
    public void removeItemAt(int adapterPosition) {

    }

    @Override
    public void clear() {
        mList.clear();
        mUniqueMapping.clear();
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public int indexOfImpl(SortedItem sortedItem) {
        return mList.indexOf(sortedItem);
    }

    @Override
    public SortedItem get(String key) {
        return mUniqueMapping.get(key);
    }

    @Override
    public int inBetweenItemCount(int adapterPosition, String itemId) {
        SortedItem sortedItem = get(itemId);
        if (sortedItem == null) {
            return 0;
        }
        int parentPosition = indexOfImpl(sortedItem);
        return adapterPosition - parentPosition;
    }

    @Override
    public int inBetweenItemCount(String keyOne, String keyTwo) {
        SortedItem sortedItemOne = get(keyOne);
        SortedItem sortedItemTwo = get(keyTwo);
        if (sortedItemTwo == null) {
            return 0;
        }
        if (sortedItemOne == null) {
            return 0;
        }
        int positionOne = indexOfImpl(sortedItemOne);
        int positionTwo = indexOfImpl(sortedItemTwo);
        return Math.abs(positionOne - positionTwo);
    }

    @Override
    public SortedItem get(int position) {
        return mList.get(position);
    }

    public void setAdapterCallback(SortedListAdapterCallback<SortedItem> adapterCallback) {
        mList = new SortedList<>(SortedItem.class, adapterCallback);
    }

    @Override
    public int getItemViewType(int position) {
        int layoutId = get(position).getLayoutId();
        return getIdInterceptor().intercept(layoutId);
    }
}
