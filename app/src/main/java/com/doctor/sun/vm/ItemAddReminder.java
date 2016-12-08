package com.doctor.sun.vm;

import android.databinding.Observable;
import android.support.annotation.NonNull;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rick on 30/7/2016.
 */

public class ItemAddReminder extends BaseItem {
    private int opCount = 1;
    private int itemSize = 1;

    public void addReminder(final SortedListAdapter adapter) {

        ItemPickDate parcelable = createReminder();
        adapter.insert(parcelable);
        notifyChange();
    }

    @NonNull
    public ItemPickDate createReminder() {
        ItemPickDate parcelable = new ItemPickDate(R.layout.item_reminder2, "", 0);
        parcelable.setPosition(getPosition() - QuestionsModel.PADDING + 3 + opCount);
        parcelable.setSubPosition(itemSize());
        parcelable.setItemId(UUID.randomUUID().toString());
        registerItemChangedListener(parcelable);
        opCount += 1;
        itemSize += 1;
        return parcelable;
    }

    public void registerItemChangedListener(ItemPickDate parcelable) {
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
        return adapter.inBetweenItemCount(thisPosition, getKey().replace(QuestionType.reminder, ""));
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_add_reminder;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (!isEnabled()) {
            return null;
        }

        int adapterPosition = adapter.indexOfImpl(this);
        String key = getKey().replace(QuestionType.reminder, "");
        int distance = adapter.inBetweenItemCount(adapterPosition, key);
        String questionId = getQuestionId(adapter, key);
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

    public String getQuestionId(SortedListAdapter adapter, String key) {
        Questions2 item = (Questions2) adapter.get(key);
        return item.answerId;
    }

    public int itemSize() {
        return itemSize - 1;
    }
}
