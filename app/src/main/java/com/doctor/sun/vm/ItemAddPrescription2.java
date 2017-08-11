package com.doctor.sun.vm;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.doctor.sun.ui.fragment.EditPrescriptionsFragment;
import com.doctor.sun.util.JacksonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by rick on 30/7/2016.
 */

public class ItemAddPrescription2 extends BaseItem {
    private int opCount = -1;
    private int itemSize = -1;

    public void addDrug(Context context, final SortedListAdapter adapter) {
        Bundle args = EditPrescriptionsFragment.getArgs(null, false);
        Intent intent = SingleFragmentActivity.intentFor(context, "添加/编辑用药", args);
        Messenger messenger = new Messenger(new Handler(new Callback(this, adapter)));
        intent.putExtra(Constants.HANDLER, messenger);
        context.startActivity(intent);
    }

    public void addPrescription(Prescription parcelable, SortedListAdapter adapter) {
        if (opCount == -1) {
            opCount = inBetweenItemCount(adapter) + 1;
            itemSize = opCount;
        }

        parcelable.setPosition(getPosition() - QuestionsModel.PADDING + 3 + opCount);
        parcelable.setItemId(UUID.randomUUID().toString());
        registerItemChangedListener(parcelable);
        adapter.insert(parcelable);
        notifyChange();
        opCount += 1;
        itemSize += 1;
    }

    public void registerItemChangedListener(final Prescription parcelable) {
        parcelable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.removed) {
                    itemSize -= 1;
                    notifyChange();
                }
            }
        });
        addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                parcelable.setEnabled(isEnabled());
            }
        });
    }

    public int inBetweenItemCount(SortedListAdapter adapter) {
        int thisPosition = adapter.indexOfImpl(this);
        return adapter.inBetweenItemCount(thisPosition, getKey().replace(QuestionType.drug, ""));
    }

    public boolean sizeLessThen(int i, SortedListAdapter adapter) {
        return inBetweenItemCount(adapter) - 1 < i;
    }

    @Override
    public int getLayoutId() {
        if (!isVisible()) {
            return R.layout.item_empty;
        }
        return R.layout.item_add_prescription3;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {
        if (!isEnabled()) {
            return null;
        }

        int adapterPosition = adapter.indexOfImpl(this);
        String key = getKey().replace(QuestionType.drug, "");
        int distance = adapter.inBetweenItemCount(adapterPosition, key);
        String questionId = getQuestionId(adapter, key);
        if (distance <= 1) {
            return null;
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("question_id", questionId);
        ArrayList<Prescription> hashMaps = new ArrayList<>();
        for (int i = distance; i > 1; i--) {
            int index = adapterPosition - i + 1;
            try {
                Prescription sortedItem = (Prescription) adapter.get(index);
                hashMaps.add(sortedItem);
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

    private static class Callback implements Handler.Callback {

        private ItemAddPrescription2 item;
        private SortedListAdapter adapter;

        public Callback(ItemAddPrescription2 item, SortedListAdapter adapter) {
            this.item = item;
            this.adapter = adapter;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (item.opCount == -1) {
                item.opCount = item.inBetweenItemCount(adapter) + 1;
                item.itemSize = item.opCount;
            }
            switch (msg.what) {
                case DiagnosisFragment.EDIT_PRESCRITPION: {
                    String jsonStr = msg.getData().getString(Constants.DATA);
                    Gson gson = new GsonBuilder().create();
                    Prescription parcelable = JacksonUtils.fromJson(jsonStr, Prescription.class);
                    if (parcelable == null) return false;
                    item.addPrescription(parcelable, adapter);
                }
            }
            item = null;
            adapter = null;
            return false;
        }
    }
}
