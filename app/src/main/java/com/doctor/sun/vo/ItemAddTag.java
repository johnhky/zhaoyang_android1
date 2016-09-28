package com.doctor.sun.vo;

import android.databinding.Observable;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rick on 23/8/2016.
 */

public class ItemAddTag extends BaseItem {
    private static final int PADDING = 1000;
    public static final String TAGS_START = "TAGS_START";
    public static final String TAGS_END = "TAGS_END";
    private int opCount = -1;
    private int itemSize = -1;

    public ItemAddTag() {
        setItemId(TAGS_END);
    }

    public void addTag(final SortedListAdapter adapter) {
        if (opCount == -1) {
            opCount = inBetweenItemCount(adapter);
            itemSize = opCount;
        }

        ItemTextInput2 parcelable = new ItemTextInput2(R.layout.item_reminder2, "", "");
        parcelable.setPosition(getPosition() + PADDING + 2 + opCount);
        parcelable.setItemId(UUID.randomUUID().toString());
        registerItemChangedListener(parcelable);
        adapter.insert(parcelable);
        notifyChange();
        opCount += 1;
        itemSize += 1;
    }

    private void registerItemChangedListener(BaseItem parcelable) {
        parcelable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.removed) {
                    itemSize -= 1;
                    notifyChange();
                }
            }
        });
    }

    public int inBetweenItemCount(SortedListAdapter adapter) {
        int thisPosition = adapter.indexOfImpl(this);
        return adapter.inBetweenItemCount(thisPosition, TAGS_START);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_empty;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {

        int adapterPosition = adapter.indexOfImpl(this);
        int distance = adapter.inBetweenItemCount(adapterPosition, TAGS_START);
        if (distance <= 1) {
            return null;
        }
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = distance; i > 1; i--) {
            int index = adapterPosition - i + 1;
            try {
                SortedItem item = adapter.get(index);
                String value = item.getValue();
                if (!Strings.isNullOrEmpty(value)) {
                    arrayList.add(value);
                }
            } catch (ClassCastException ignored) {
                ignored.printStackTrace();
            }
        }
        result.put("tags", arrayList);

        return result;
    }

}
