package com.doctor.sun.model;

import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DoctorInfo;
import com.doctor.sun.entity.MedicineInfo;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemButton;
import com.doctor.sun.vo.ItemRadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kb on 16-9-15.
 */

public class PayPrescriptionsModel {
    public static final String TAG = PayPrescriptionsModel.class.getSimpleName();

    public List<SortedItem> parseData(Address address, DoctorInfo doctorInfo, MedicineInfo medicineInfo) {
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

        //TODO ClickMenu
        ClickMenu selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, "已使用优惠券", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "选择优惠券", Toast.LENGTH_SHORT).show();
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

        ItemRadioGroup payMethod = new ItemRadioGroup(R.layout.item_pick_pay_method);
        payMethod.setItemId("payMethod");
        payMethod.setPosition(result.size());
        result.add(payMethod);

        ItemButton confirmButton = new ItemButton(R.layout.item_big_button, "确定") {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "调用支付接口", Toast.LENGTH_SHORT).show();
            }
        };
        confirmButton.setItemId("confirmButton");
        confirmButton.setPosition(result.size());
        result.add(confirmButton);

        return result;
    }

    private void insertDivider(List<SortedItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px2);
        item.setItemId(UUID.randomUUID().toString());
        item.setPosition(list.size());
        list.add(item);
    }
}
