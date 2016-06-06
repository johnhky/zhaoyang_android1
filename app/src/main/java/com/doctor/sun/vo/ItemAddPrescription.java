package com.doctor.sun.vo;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;

/**
 * Created by rick on 4/6/2016.
 */
public class ItemAddPrescription implements LayoutId {
    private SimpleAdapter<LayoutId, ViewDataBinding> layoutIds;


    public View.OnClickListener addDrug(final BaseAdapter adapter, final RecyclerView.ViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditPrescriptionActivity.makeIntent(v.getContext(), null);
                Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                            case DiagnosisFragment.EDIT_PRESCRITPION: {
                                Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                                ItemAddPrescription answer = (ItemAddPrescription) adapter.get(vh.getAdapterPosition());

                                answer.layoutIds.add(parcelable);
                                answer.layoutIds.notifyItemInserted(answer.layoutIds.size());
                                adapter.set(vh.getAdapterPosition(), answer);
                                adapter.notifyItemChanged(vh.getAdapterPosition());
                            }
                        }
                        return false;
                    }
                }));
                intent.putExtra(Constants.HANDLER, messenger);
                v.getContext().startActivity(intent);
            }
        };
    }

    public boolean sizeLessThen(int i) {
        return layoutIds.size() < i;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_add_prescription2;
    }

    public SimpleAdapter<LayoutId, ViewDataBinding> getAdapter(Context context) {
        if (layoutIds == null) {
            layoutIds = new SimpleAdapter<>(context);
            layoutIds.onFinishLoadMore(true);
        }
        return layoutIds;
    }
}
