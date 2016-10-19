package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;

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
        Intent intent = EditPrescriptionActivity.makeIntent(context, null);
        Messenger messenger = new Messenger(new Handler(new Callback(this, adapter)));
        intent.putExtra(Constants.HANDLER, messenger);
        context.startActivity(intent);
    }

    public void addPrescription(Prescription parcelable, SortedListAdapter adapter) {
        if (opCount == -1) {
            opCount = inBetweenItemCount(adapter) + 1;
            itemSize = opCount;
        }

        parcelable.position = getPosition() - QuestionsModel.PADDING + 3 + opCount;
        parcelable.itemId = UUID.randomUUID().toString();
        registerItemChangedListener(parcelable);
        adapter.insert(parcelable);
        notifyChange();
        opCount += 1;
        itemSize += 1;
    }

    public void registerItemChangedListener(Prescription parcelable) {
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

        int adapterPosition = adapter.indexOfImpl(this);
        String key = getKey().replace(QuestionType.drug, "");
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
                Prescription sortedItem = (Prescription) adapter.get(index);
                hashMaps.add(sortedItem.toHashMap());
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
                    Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
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
