package com.doctor.sun.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.PItemDrugBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.PayInterface;

import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/22/16.
 */
public class DrugOrderAdapter extends SimpleAdapter {
    private DrugModule api = Api.of(DrugModule.class);
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);

    public DrugOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder vh, int position) {
        if (vh.getItemViewType() == R.layout.p_item_drug) {
            final PItemDrugBinding binding = (PItemDrugBinding) vh.getBinding();
            final Drug drug = (Drug) get(vh.getAdapterPosition());

            if (drug.getRemark().equals("")) {
//                binding.llyRemark.setVisibility(View.GONE);
            }
            if (drug.getStatuses().equals("已支付")) {
                binding.tvStatus.setTextColor(Color.parseColor("#88cb5a"));
//                binding.llyLogistic.setVisibility(View.VISIBLE);
//                binding.llySelector.setVisibility(View.GONE);
            }
            if (drug.getStatuses().equals("未支付")) {
                binding.tvStatus.setTextColor(Color.parseColor("#f76d02"));
//                binding.llyLogistic.setVisibility(View.GONE);
//                binding.llySelector.setVisibility(View.VISIBLE);
            }
            if (drug.getStatuses().equals("已关闭")) {
                binding.tvStatus.setTextColor(Color.parseColor("#898989"));
//                binding.llyLogistic.setVisibility(View.GONE);
//                binding.llySelector.setVisibility(View.GONE);
            }

            binding.flCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    api.cancelOrder(drug.getId()).enqueue(new ApiCallback<String>() {
                        @Override
                        protected void handleResponse(String response) {

                        }

                        @Override
                        protected void handleApi(ApiDTO<String> body) {
//                            binding.llyLogistic.setVisibility(View.GONE);
//                            binding.llySelector.setVisibility(View.GONE);
                            binding.tvStatus.setText("已关闭");
                            binding.tvStatus.setTextColor(Color.parseColor("#898989"));
                        }
                    });
                }
            });

//            final double totalFee = Double.parseDouble(drug.getMoney());
            final String totalFee = drug.getMoney();
            final HashMap<String, String> extraField = new HashMap<>();
            extraField.put("body", "drug order");
            extraField.put("drugOrderId", String.valueOf(drug.getId()));
            binding.flPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PayMethodDialog(getContext(), new PayInterface() {
                        @Override
                        public void payWithAlipay(Activity activity, String couponId) {
                            appointmentModule.buildAlipayGoodsOrder(totalFee, "alipay", extraField).enqueue(new AlipayCallback(activity, totalFee, extraField));
                        }

                        @Override
                        public void payWithWeChat(Activity activity, String couponId) {
                            appointmentModule.buildWeChatGoodsOrder(totalFee, "wechat", extraField).enqueue(new WeChatPayCallback(activity, totalFee, extraField));
                        }

                        @Override
                        public void simulatedPay(BaseAdapter component, View view, BaseViewHolder vh) {
                            ToastHelper.showMessage(view.getContext(), "模拟支付暂时未开放");
                        }
                    }).show();
                }
            });
        }
    }
}
