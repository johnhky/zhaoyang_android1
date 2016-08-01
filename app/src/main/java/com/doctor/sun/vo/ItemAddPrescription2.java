package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;

import java.util.UUID;

/**
 * Created by rick on 30/7/2016.
 */

public class ItemAddPrescription2 extends BaseObservable implements SortedItem {
    private long position;
    private String itemId;
    private int opCount = -1;

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
                        parcelable.position = position - QuestionsModel.PADDING + 2 + opCount;
                        parcelable.itemId = UUID.randomUUID().toString();
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
        return adapter.inBetweenItemCount(thisPosition, itemId.replace("drug", ""));
    }

    public boolean sizeLessThen(int i, SortedListAdapter adapter) {
        return inBetweenItemCount(adapter) - 1 < i;
    }


    public void setPosition(long position) {
        this.position = position;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_add_prescription3;
    }

    @Override
    public long getCreated() {
        return -position;
    }

    @Override
    public String getKey() {
        return itemId;
    }

    @Override
    public float getSpan() {
        return 1;
    }

    @Override
    public String toJson(SortedListAdapter adapter) {
        return "";
    }
}
