package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ItemConsultedBinding;
import com.doctor.sun.databinding.PItemConsultedBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rick on 12/17/15.
 */
public class ConsultedAdapter extends SimpleAdapter<AppointMent, ViewDataBinding> {
    private long startTime;
    private final Realm realm;

    public ConsultedAdapter(Context context, Realm realm, long startTime) {
        super(context);
        this.realm = realm;
        this.startTime = startTime;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, int position) {
        if (vh.getItemViewType() == R.layout.item_consulted) {
            AppointMent appointment = (AppointMent) get(position);
            RealmQuery<TextMsg> q = realm.where(TextMsg.class)
                    .equalTo("sessionId", appointment.getVoipAccount())
                    .equalTo("userData", appointment.getHandler().getUserData());
            RealmResults<TextMsg> results = q.findAll();
            long count = q.equalTo("haveRead", false).count();
            ItemConsultedBinding rootBinding = (ItemConsultedBinding) vh.getBinding();
            bindCount(results, count, rootBinding.tvMessageCount);
        } else if (vh.getItemViewType() == R.layout.p_item_consulted) {
            AppointMent appointment = (AppointMent) get(position);
            RealmQuery<TextMsg> q = realm.where(TextMsg.class)
                    .equalTo("sessionId", appointment.getDoctor().getVoipAccount())
                    .equalTo("userData", appointment.getHandler().getUserData());
            ;
            RealmResults<TextMsg> results = q.findAll();
            long count = q.equalTo("haveRead", false).count();
            PItemConsultedBinding rootBinding = (PItemConsultedBinding) vh.getBinding();
            bindCount(results, count, rootBinding.tvMessageCount);
        }
    }

    private void bindCount(RealmResults<TextMsg> results, long count, TextView tvMessageCount) {
        if (!results.isEmpty()) {
            if (count != 0l) {
                tvMessageCount.setVisibility(View.VISIBLE);
                tvMessageCount.setText(String.valueOf(count));
            } else {
                tvMessageCount.setVisibility(View.GONE);
            }
        } else {
            tvMessageCount.setVisibility(View.GONE);
        }
    }
}
