package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
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
    private ArrayList<Prescription> prescriptions = new ArrayList<>();

    public void addDrug(Context context, final SortedListAdapter adapter) {
        Intent intent = EditPrescriptionActivity.makeIntent(context, null);
        Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (opCount == -1) {
                    opCount = inBetweenItemCount(adapter);
                }
                switch (msg.what) {
                    case DiagnosisFragment.EDIT_PRESCRITPION: {
                        Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                        parcelable.position = getPosition() - QuestionsModel.PADDING + 2 + opCount;
                        parcelable.itemId = UUID.randomUUID().toString();
                        prescriptions.add(parcelable);
                        adapter.insert(parcelable);
                        notifyChange();
                        opCount += 1;
                    }
                }
                return false;
            }
        }));
        intent.putExtra(Constants.HANDLER, messenger);
        context.startActivity(intent);
    }

    public int inBetweenItemCount(SortedListAdapter adapter) {
        int thisPosition = adapter.indexOf(this);
        return adapter.inBetweenItemCount(thisPosition, getKey().replace(QuestionType.drug, ""));
    }

    public boolean sizeLessThen(int i, SortedListAdapter adapter) {
        return inBetweenItemCount(adapter) - 1 < i;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_add_prescription3;
    }


    @Override
    public int getSpan() {
        return 12;
    }

    @Override
    public HashMap<String, Object> toJson(SortedListAdapter adapter) {

        int adapterPosition = adapter.indexOf(this);
        String questionId = getKey().replace(QuestionType.drug, "");
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
                Prescription sortedItem = (Prescription) adapter.get(index);
                hashMaps.add(sortedItem.toHashMap());
            } catch (ClassCastException ignored) {
                ignored.printStackTrace();
            }
        }
        result.put("fill_content", hashMaps);

        return result;
    }
}
