package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.IncludeMessageCountBinding;
import com.doctor.sun.databinding.ItemConsultingBinding;
import com.doctor.sun.databinding.PItemConsultingBinding;
import com.doctor.sun.databinding.PItemMedicineHelperBinding;
import com.doctor.sun.databinding.PItemSystemTipBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.ui.activity.patient.MedicineHelperActivity;
import com.doctor.sun.ui.activity.patient.SystemMsgListActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import io.ganguo.library.Config;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rick on 12/15/15.
 */
public class ConsultingAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private PushModule api = Api.of(PushModule.class);

    public ConsultingAdapter(Context context, Realm realm) {
        super(context);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ViewDataBinding> vh, final int position) {
        if (vh.getItemViewType() == R.layout.p_item_system_tip) {
            final PItemSystemTipBinding binding = (PItemSystemTipBinding) vh.getBinding();
            api.systemMsg(String.valueOf(1)).enqueue(new ApiCallback<PageDTO<SystemMsg>>() {
                @Override
                protected void handleResponse(PageDTO<SystemMsg> response) {
                    if (response.getData().size() > 0) {
                        binding.tvDate.setText(response.getData().get(0).getCreatedAt());
                        binding.tvTip.setText(response.getData().get(0).getTitle());
                    }
                    long lastVisitTime = Config.getLong(SystemMsgListActivity.LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT), -1);
//                    Log.e(TAG, "lastVisitTime: " + lastVisitTime);
                    int count = 0;
                    for (SystemMsg systemTip : response.getData()) {
                        boolean haveRead = systemTip.getHandler().haveRead(lastVisitTime);
                        if (!haveRead) {
                            count += 1;
                        }
                    }
                    if (count > 0) {
                        binding.tvCount.setText(String.valueOf(count));
                        binding.tvCount.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvCount.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
