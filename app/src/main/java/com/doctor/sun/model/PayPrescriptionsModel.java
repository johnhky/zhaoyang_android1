package com.doctor.sun.model;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.constans.DrugStatus;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.immutables.DrugOrderDetail;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.activity.patient.PConsultingActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.util.PreferenceHelper;
import com.doctor.sun.vm.ItemButton;
import com.doctor.sun.vm.ItemCoupons;
import com.doctor.sun.vm.ItemDrugDetailBtn;
import com.doctor.sun.vm.ItemRadioGroup;
import com.doctor.sun.vm.ItemTextInput2;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kb on 16-9-15.
 */

public class PayPrescriptionsModel {
    public static final String TAG = PayPrescriptionsModel.class.getSimpleName();


    private ItemRadioGroup payMethod;
    private double money = 0D;

    private HashMap<String, String> extraField;

    public List<SortedItem> parseData(final Context context, final DrugOrderDetail response) {
        final DrugExtraFee extra = response.getCharge();
        boolean hasPay = response.getHas_pay() == IntBoolean.TRUE;
        money = response.getNeed_pay();
        List<SortedItem> result = new ArrayList<>();

        extraField = DrugListFragment.getDrugExtraField();

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        String receiverName = response.getTo() == null || response.getTo().equals("") ? "收件人" : response.getTo();
        String phoneNumber = response.getPhone() == null || response.getPhone().equals("") ? "手机号码:" : response.getPhone();
        String addressStr = response.getAddress() == null || response.getAddress().equals("") ? "收货地址:" :"收货地址:"+ response.getAddress();
        ItemTextInput2 address = new ItemTextInput2(R.layout.item_address_update, "", "");
        address.setPosition(result.size());
        address.setItemId("address");
        address.setResult(response.getId());
        if (response.getHas_pay()==1){
            address.setEnabled(false);
        }
        address.setProvince(addressStr);
        address.setPhone(phoneNumber);
        address.setReceiver(receiverName);
        address.setRemark(response.getRemark());
        result.add(address);

        ItemTextInput2 drugName = new ItemTextInput2(R.layout.item_drug_detail_btn, "药品：", "");
        drugName.setItemId("药品品类");
        drugName.setPosition(result.size());
        /*drugName.setAdapter(drugDetailAdapter(response, extra));*/
        result.add(drugName);


        if (!response.getDrug().isEmpty()) {

            for (Drug.DrugEntity s : response.getDrug_detail()) {

                ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_drug_spec, s.drug + s.specification, "");
                itemTextInput2.setSubTitle(s.drug_num);
                itemTextInput2.setTitleGravity(Gravity.START);
                itemTextInput2.setItemId(itemTextInput2.getTitle() + itemTextInput2.getSubTitle());
                itemTextInput2.setPosition(result.size());
                result.add(itemTextInput2);
            }
            ModelUtils.insertDividerNoMargin(result);
            ItemTextInput2 fee = new ItemTextInput2(R.layout.item_totalfee, "￥" + response.getDrug_money(), "");
            fee.setResult("省￥" + response.getSave_money());
            fee.setItemId("药费");
            fee.setPosition(result.size());
            result.add(fee);
        }
        //下面这些都放在弹窗里面显示
        ModelUtils.insertSpace(result, R.layout.blue_space_8dp);

        if (!hasPay) {
            extraField = DrugListFragment.getDrugExtraField();
        }

        final ItemCoupons selectCoupon = new ItemCoupons(response);
        selectCoupon.setItemLayoutId(R.layout.item_select_coupon_new);
        selectCoupon.setItemId("selectCoupon");
        selectCoupon.setPosition(result.size());
        result.add(selectCoupon);

        ModelUtils.insertDividerNoMargin(result);

        ItemTextInput2 save = new ItemTextInput2(R.layout.item_save_timenfee, "", "");
        save.setPosition(result.size());
        save.setItemId("save");
        result.add(save);
        ModelUtils.insertDividerNoMargin(result);
        if (!response.getCharge().discount.isEmpty()) {
            for (String s : response.getCharge().discount) {
                ItemTextInput2 discount = new ItemTextInput2(R.layout.item_drug_discount, s, "");
                discount.setResult("优惠");
                discount.setPosition(result.size());
                discount.setItemId("discount");
                result.add(discount);
            }
        }
        if (response.getCharge().commission.isEmpty()) {
                ItemTextInput2 discount = new ItemTextInput2(R.layout.item_drug_discount, "免收服务费减￥"+response.getService_fee(), "");
                discount.setResult("减免");
                discount.setPosition(result.size());
                discount.setItemId("commission");
                result.add(discount);
        }
        ModelUtils.insertSpace(result, R.layout.divider_30dp);
        //当没有收取服务费的时候，就要显示优惠了服务费

        if (response.getStatus().equals(DrugStatus.UNCERTAIN)) {

            ModelUtils.insertDividerNoMargin(result);
            ModelUtils.insertSpace(result, R.layout.space_8dp);

            ItemButton confirmDrugButton = new ItemButton(R.layout.item_big_button, "跳转寄药小组手确认药单") {
                @Override
                public void onClick(View view) {
                    Intent intent = PConsultingActivity.makeIntent(context);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    Intent medicineStore = MedicineStoreActivity.makeIntent(context);
                    context.startActivity(medicineStore);
                }
            };
            confirmDrugButton.setItemId("confirmDrugButton");
            confirmDrugButton.setPosition(result.size());
            result.add(confirmDrugButton);
        } else if (!hasPay) {
            ModelUtils.insertDividerNoMargin(result);
            final ItemTextInput2 pay = new ItemTextInput2(R.layout.item_pay_truth, "收费明细", "");
            pay.setPosition(result.size());
            pay.setItemId("pay");
            pay.setProvince("￥");
            pay.setResult(money + "");
            pay.setRemark("确认付款");
            pay.setEnabled(true);
            pay.setBackground(R.color.blue_33);
            pay.setAdapter(drugDetailAdapter(response, extra));
            /*pay.setData(response, selectCoupon);*/
            result.add(pay);
            selectCoupon.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    double shouldPay = response.getNeed_pay() - selectCoupon.getDiscountMoney();
                    if (shouldPay < 0D) {
                        shouldPay = 0D;
                    }
                    BigDecimal bigDecimal = new BigDecimal(shouldPay).setScale(2, BigDecimal.ROUND_UP);
                    money = bigDecimal.doubleValue();
                    pay.setResult(money + "");
                }
            });
        } else if (hasPay) {
            ModelUtils.insertDividerNoMargin(result);
            final ItemTextInput2 pay = new ItemTextInput2(R.layout.item_pay_truth, "收费明细", "");
            pay.setPosition(result.size());
            pay.setItemId("pay");
            pay.setRemark("已付款");
            pay.setEnabled(false);
            pay.setBackground(R.color.button_press);
            pay.setResult("￥" + money + "");
            pay.setAdapter(drugDetailAdapter(response, extra));
            /*pay.setData(response, selectCoupon);*/
            result.add(pay);
        }

        return result;
    }


    public BaseListAdapter<SortedItem, ViewDataBinding> drugDetailAdapter(DrugOrderDetail response, DrugExtraFee extra) {
        //下面这些都放在弹窗里面显示

        List<SortedItem> result = new ArrayList<>();


        ItemDrugDetailBtn drugName = new ItemDrugDetailBtn(R.layout.item_text_h2, "药品收费：", "");
        drugName.setItemId("药品品类");
        drugName.setPosition(result.size());
        result.add(drugName);

        if (!response.getDrug().isEmpty()) {
            for (int i = 0; i < response.getDrug_detail().size(); i++) {
                Drug.DrugEntity s = response.getDrug_detail().get(i);
                ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_drug_spec2, (i + 1) + "." + s.drug + s.specification, "");
                itemTextInput2.setResult(s.price + "*" + s.drug_num);
                itemTextInput2.setSubTitle("合计：" + s.money+"元");
                itemTextInput2.setTitleGravity(Gravity.START);
                itemTextInput2.setItemId(itemTextInput2.getTitle() + itemTextInput2.getSubTitle());
                itemTextInput2.setPosition(result.size());
                result.add(itemTextInput2);
            }

            ModelUtils.insertSpace(result, R.layout.space_8dp);
        }

        if (!extra.extraFee.isEmpty() || !extra.commission.isEmpty()) {
            ModelUtils.insertSpace(result, R.layout.space_8dp);
            ModelUtils.insertDividerNoMargin(result);
            ModelUtils.insertSpace(result, R.layout.space_8dp);

            ItemTextInput2 itemText = new ItemTextInput2(R.layout.item_text_h2, "其他收费：", "");
            itemText.setItemId("其他收费");
            itemText.setPosition(result.size());
            result.add(itemText);

            ModelUtils.insertSpace(result, R.layout.space_8dp);

            if (!extra.extraFee.isEmpty()) {
                for (String s : extra.extraFee) {
                    ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_r_grey_text, s, "");
                    itemTextInput2.setTitleGravity(Gravity.START);
                    itemTextInput2.setItemId(s);
                    itemTextInput2.setPosition(result.size());
                    result.add(itemTextInput2);
                }
            }
            if (!extra.commission.isEmpty()) {
                for (String s : extra.commission) {
                    ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_r_grey_text, s, "");
                    itemTextInput2.setTitleGravity(Gravity.START);
                    itemTextInput2.setItemId(s);
                    itemTextInput2.setPosition(result.size());
                    result.add(itemTextInput2);
                }
            }
            ModelUtils.insertSpace(result, R.layout.space_8dp);
        }

        if (!extra.discount.isEmpty()) {
            Description discount = new Description(R.layout.item_description, "寄药优惠");
            discount.setItemId("discount");
            discount.setPosition(result.size());
            result.add(discount);
            ModelUtils.insertSpace(result, R.layout.space_8dp);
            for (String s : extra.discount) {
                ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_r_grey_text, s, "");
                itemTextInput2.setTitleGravity(Gravity.START);
                itemTextInput2.setItemId(s);
                itemTextInput2.setPosition(result.size());
                result.add(itemTextInput2);
            }
            ModelUtils.insertSpace(result, R.layout.space_8dp);
        }
        SortedListAdapter<ViewDataBinding> adapter = new SortedListAdapter<>();
        adapter.insertAll(result);
        return adapter;
    }


}
