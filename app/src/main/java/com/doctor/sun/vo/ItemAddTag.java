package com.doctor.sun.vo;

import android.databinding.Observable;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rick on 23/8/2016.
 */

public class ItemAddTag extends BaseItem{
    private static final int PADDING = 1000;
    private int opCount = -1;
    private int itemSize = -1;

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
        return adapter.inBetweenItemCount(thisPosition,"TAGS_START");
    }
    @Override
    public int getLayoutId() {
        return R.layout.item_empty;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {

        int adapterPosition = adapter.indexOfImpl(this);
        String questionId = getKey().replace(QuestionType.reminder, "");
        int distance = adapter.inBetweenItemCount(adapterPosition, questionId);
        if (distance <= 1) {
            return null;
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("question_id", questionId);
        ArrayList<HashMap<String, String>> hashMaps = new ArrayList<>();
        for (int i = distance; i > 1; i--) {
            int index = adapterPosition - i + 1;
            try {
                ItemPickDate itemPickDate = (ItemPickDate) adapter.get(index);

                HashMap<String, String> object = new HashMap<>();
                object.put("time", itemPickDate.getDate());
                object.put("content", itemPickDate.getTitle());

                hashMaps.add(object);
            } catch (ClassCastException ignored) {
                ignored.printStackTrace();
            }
        }
        result.put("fill_content", hashMaps);

        return result;
    }

    public int itemSize() {
        return itemSize - 1;
    }
}
