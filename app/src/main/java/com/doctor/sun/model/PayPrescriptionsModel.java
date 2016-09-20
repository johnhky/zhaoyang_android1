package com.doctor.sun.model;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.MobEventId;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DoctorInfo;
import com.doctor.sun.entity.MedicineInfo;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemButton;
import com.doctor.sun.vo.ItemRadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by kb on 16-9-15.
 */

public class PayPrescriptionsModel extends BaseObservable{
    public static final String TAG = PayPrescriptionsModel.class.getSimpleName();

    private ProfileModule api;
    private AppointmentModule payApi;

    private List<Coupon> coupons;
    private int selectedCoupon = -1;

    public PayPrescriptionsModel() {
        api = Api.of(ProfileModule.class);
        payApi = Api.of(AppointmentModule.class);
    }

    public List<SortedItem> parseData(Address address, DoctorInfo doctorInfo, final MedicineInfo medicineInfo) {
        List<SortedItem> result = new ArrayList<>();

        address.setItemId("address");
        address.setPosition(result.size());
        result.add(address);

        Description detailDescription = new Description(R.layout.item_description, "寄药详情");
        detailDescription.setItemId("detailDescription");
        detailDescription.setPosition(result.size());
        result.add(detailDescription);

        doctorInfo.setItemId("doctorInfo");
        doctorInfo.setPosition(result.size());
        result.add(doctorInfo);

        insertDivider(result);

        medicineInfo.setItemId("medicineInfo");
        medicineInfo.setPosition(result.size());
        result.add(medicineInfo);

        Description couponDescription = new Description(R.layout.item_description, "优惠券");
        couponDescription.setItemId("couponDescription");
        couponDescription.setPosition(result.size());
        result.add(couponDescription);

        ClickMenu selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, "未使用优惠券", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoupon(v.getContext());
            }
        });
        selectCoupon.setSubTitle("点击选择");
        selectCoupon.setItemId("selectCoupon");
        selectCoupon.setPosition(result.size());
        result.add(selectCoupon);

        Description payDescription = new Description(R.layout.item_description, "支付方式");
        payDescription.setItemId("payDescription");
        payDescription.setPosition(result.size());
        result.add(payDescription);

        final ItemRadioGroup payMethod = new ItemRadioGroup(R.layout.item_pick_pay_method);
        payMethod.setItemId("payMethod");
        payMethod.setPosition(result.size());
        result.add(payMethod);

        ItemButton confirmButton = new ItemButton(R.layout.item_big_button, "确定") {
            @Override
            public void onClick(View view) {

                HashMap<String, String> extraField = DrugListFragment.getDrugExtraField();
                extraField.put("medicineOrderId", medicineInfo.getMedicineOrderId());

                switch (payMethod.getSelectedItem()) {
                    case PayMethod.ALIPAY:
                        payApi.buildAlipayGoodsOrder(medicineInfo.getMedicinePrice(), "alipay", extraField)
                                .enqueue(new AlipayCallback((Activity) view.getContext(), medicineInfo.getMedicinePrice(), extraField));
                        break;
                    case PayMethod.WECHAT:
                        payApi.buildWeChatGoodsOrder(medicineInfo.getMedicinePrice(), "wechat", extraField)
                                .enqueue(new WeChatPayCallback((Activity) view.getContext(), medicineInfo.getMedicinePrice(), extraField));
                        break;
                    case PayMethod.SIMULATED:
                        Toast.makeText(view.getContext(), "模拟支付", Toast.LENGTH_SHORT).show();
                }
            }
        };
        confirmButton.setItemId("confirmButton");
        confirmButton.setPosition(result.size());
        result.add(confirmButton);

        loadCoupons();

        return result;
    }

    private void insertDivider(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px2);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }

    private void selectCoupon(Context context) {
        if (coupons == null || coupons.isEmpty()) {
            Toast.makeText(context, "暂时没有可以使用的优惠券", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(context)
                .title("选择优惠券(单选)")
                .items(coupons)
                .itemsCallbackSingleChoice(selectedCoupon, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedCoupon = which;
                        notifyChange();
                        return true;
                    }
                })
                .build().show();
    }

    private void loadCoupons() {
        api.coupons(CouponType.CAN_USE_NOW, Coupon.Scope.DRUG_ORDER, 200).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons = response;
                } else {
                    coupons = null;
                }
                notifyChange();
            }
        });
    }

    private String getCouponId() {
        return coupons.get(selectedCoupon).id;
    }
}
