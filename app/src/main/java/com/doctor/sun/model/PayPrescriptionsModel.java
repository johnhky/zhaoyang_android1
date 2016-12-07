package com.doctor.sun.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.DrugStatus;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemButton;
import com.doctor.sun.vo.ItemRadioGroup;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kb on 16-9-15.
 */

public class PayPrescriptionsModel {
    public static final String TAG = PayPrescriptionsModel.class.getSimpleName();

    private ProfileModule api;

    private List<Coupon> coupons;
    private int selectedCoupon = -1;

    private ClickMenu selectCoupon;
    private ItemRadioGroup payMethod;
    private ItemTextInput2 shouldPayMoney;
    private double money;
    private Drug drug;

    private HashMap<String, String> extraField;

    public PayPrescriptionsModel() {
        api = Api.of(ProfileModule.class);
    }


    public List<SortedItem> parseData(final Context context, final Drug response) {

        drug = response;
        final DrugExtraFee extra = response.getExtra_fee();
        boolean hasPay = response.getHas_pay() == IntBoolean.TRUE;
        money = response.getMoney();

        List<SortedItem> result = new ArrayList<>();

        extraField = DrugListFragment.getDrugExtraField();

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        ItemTextInput2 receiver = new ItemTextInput2(R.layout.item_text_subtitle, "    收件人：", "");
        String receiverName = response.getTo() == null || response.getTo().equals("") ? "(未填写)" : response.getTo();
        receiver.setSubTitle(receiverName);
        receiver.setItemId("receiver");
        receiver.setPosition(result.size());
        result.add(receiver);

        ItemTextInput2 phone = new ItemTextInput2(R.layout.item_text_subtitle, "手机号码：", "");
        String phoneNumber = response.getPhone() == null || response.getPhone().equals("") ? "(未填写)" : response.getPhone();
        phone.setSubTitle(phoneNumber);
        phone.setItemId("phone");
        phone.setPosition(result.size());
        result.add(phone);

        ItemTextInput2 address = new ItemTextInput2(R.layout.item_text_subtitle, "收货地址：", "");
        address.setSubTitle(response.getAddress());
        address.setItemId("address");
        address.setPosition(result.size());
        result.add(address);

        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_subtitle, "备注信息：", "");
        remark.setSubTitle(response.getRemark());
        remark.setItemId("remark");
        remark.setPosition(result.size());
        result.add(remark);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        Description drugDetail = new Description(R.layout.item_description, "寄药详细");
        drugDetail.setItemId("drugDetail");
        drugDetail.setPosition(result.size());
        result.add(drugDetail);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        ItemTextInput2 recordName = new ItemTextInput2(R.layout.item_text_subtitle, "处方病历：", "");
        recordName.setSubTitle(response.getRecord_name());
        recordName.setItemId("recordName");
        recordName.setPosition(result.size());
        result.add(recordName);

        ItemTextInput2 doctorName = new ItemTextInput2(R.layout.item_text_subtitle, "处方医生：", "");
        doctorName.setSubTitle(response.getDoctor().getName());
        doctorName.setItemId("doctorName");
        doctorName.setPosition(result.size());
        result.add(doctorName);

        ItemTextInput2 createdTime = new ItemTextInput2(R.layout.item_text_subtitle, "下单时间：", "");
        createdTime.setSubTitle(response.getCreated_at());
        createdTime.setItemId("createdTime");
        createdTime.setPosition(result.size());
        result.add(createdTime);

        ModelUtils.insertSpace(result, R.layout.space_8dp);
        ModelUtils.insertDividerNoMargin(result);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        ItemTextInput2 drugName = new ItemTextInput2(R.layout.item_text_h2, "药品品类：", "");
        drugName.setItemId("药品品类");
        drugName.setPosition(result.size());
        result.add(drugName);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        if (!response.getDrug().isEmpty()) {

            for (Drug.DrugEntity s : response.getDrug_detail()) {

                ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_r_grey_menu, s.drug, "");
                itemTextInput2.setSubTitle(s.price);
                itemTextInput2.setTitleGravity(Gravity.START);
                itemTextInput2.setItemId(s.toString());
                itemTextInput2.setPosition(result.size());
                result.add(itemTextInput2);
            }

            ModelUtils.insertSpace(result, R.layout.space_8dp);
        }

        ItemTextInput2 medicinePrice = new ItemTextInput2(R.layout.item_text_h2, "药单总价：￥" + response.getDrug_money(), "");
        medicinePrice.setTitleColor(R.color.color_medicine_price);
        medicinePrice.setItemId("medicinePrice");
        medicinePrice.setPosition(result.size());
        result.add(medicinePrice);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        if (!extra.extraFee.isEmpty() || !extra.commission.isEmpty()) {
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

        if (!hasPay) {
            loadCoupons(response);

            Description couponDescription = new Description(R.layout.item_description, "优惠券");
            couponDescription.setBackgroundColor(R.color.color_coupon_background_yellow);
            couponDescription.setTitleColor(R.color.white);
            couponDescription.setItemId("couponDescription");
            couponDescription.setPosition(result.size());
            result.add(couponDescription);

            extraField = DrugListFragment.getDrugExtraField();

            if (!response.getUser_coupon_id().equals("0")) {

                String couponString = "已选取" + response.getCoupon_info().couponMoney + "元优惠券";
                selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, couponString, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                selectCoupon.setEnabled(false);
                selectCoupon.setItemId("selectCoupon");
                selectCoupon.setPosition(result.size());
                result.add(selectCoupon);
            } else {
                selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectCoupon(v.getContext());
                    }
                });
                selectCoupon.setSubTitle("点击选择");
                selectCoupon.setItemId("selectCoupon");
                selectCoupon.setPosition(result.size());
                result.add(selectCoupon);
            }
        } else {
            Description couponDescription = new Description(R.layout.item_description, "优惠券");
            couponDescription.setBackgroundColor(R.color.color_coupon_background_yellow);
            couponDescription.setTitleColor(R.color.white);
            couponDescription.setItemId("couponDescription");
            couponDescription.setPosition(result.size());
            result.add(couponDescription);

            String couponString = "使用了" + response.getCoupon_info().couponMoney + "元优惠券";
            selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, couponString, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            selectCoupon.setEnabled(false);
            selectCoupon.setItemId("selectCoupon");
            selectCoupon.setPosition(result.size());
            result.add(selectCoupon);
        }

        Description total = new Description(R.layout.item_description, "总价");
        total.setItemId("total");
        total.setPosition(result.size());
        result.add(total);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        String totalMoneyString = "订单总额：￥" + money;
        final ItemTextInput2 totalMoney = new ItemTextInput2(R.layout.item_r_grey_text, totalMoneyString, "");
        totalMoney.setTextSize(R.dimen.font_12);
        totalMoney.setTitleGravity(Gravity.START);
        totalMoney.setItemId("totalMoney");
        totalMoney.setPosition(result.size());
        result.add(totalMoney);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        String shouldPayMoneyString = "<font color=\"#f65600\">实际应付：￥" + response.getNeed_pay() + "</font>";
        shouldPayMoney = new ItemTextInput2(R.layout.item_total_money, shouldPayMoneyString, "");
        shouldPayMoney.setTitleGravity(Gravity.START);
        shouldPayMoney.setItemId("shouldPayMoney");
        shouldPayMoney.setPosition(result.size());
        //当没有收取服务费的时候，就要显示优惠了服务费
        shouldPayMoney.setUserSelected(extra.commission.isEmpty());

        result.add(shouldPayMoney);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        if (response.getStatus().equals(DrugStatus.UNCERTAIN)) {

            ModelUtils.insertDividerNoMargin(result);
            ModelUtils.insertSpace(result, R.layout.space_8dp);

            ItemButton confirmDrugButton = new ItemButton(R.layout.item_big_button, "跳转寄药小组手确认药单") {
                @Override
                public void onClick(View view) {
                    Intent intent = ConsultingActivity.makeIntent(context);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            };
            confirmDrugButton.setItemId("confirmDrugButton");
            confirmDrugButton.setPosition(result.size());
            result.add(confirmDrugButton);
        } else if (!hasPay) {
            Description payDescription = new Description(R.layout.item_description, "支付方式");
            payDescription.setItemId("payDescription");
            payDescription.setPosition(result.size());
            result.add(payDescription);

            payMethod = new ItemRadioGroup(R.layout.item_pick_pay_method);
            payMethod.setItemId("payMethod");
            payMethod.setPosition(result.size());
            result.add(payMethod);

            ItemButton confirmButton = new ItemButton(R.layout.item_big_button, "确定") {
                @Override
                public void onClick(View view) {

                    if (payMethod.getSelectedItem() == -1) {
                        Toast.makeText(view.getContext(), "请选择支付方式", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String coupon;
                    if (selectedCoupon != -1) {
                        coupon = getCouponId();
                    } else {
                        coupon = "";
                    }
                    response.confirmPay(view.getContext(), payMethod.getSelectedItem(), coupon, money, extraField);
                }
            };
            confirmButton.setItemId("confirmButton");
            confirmButton.setPosition(result.size());
            result.add(confirmButton);

        } else {
            ModelUtils.insertDividerNoMargin(result);

            ItemButton button = new ItemButton(R.layout.item_grey_button, "已支付") {
                @Override
                public void onClick(View view) {

                }
            };
            button.setPosition(result.size());
            result.add(button);
        }

        return result;
    }

    private void selectCoupon(Context context) {
        if (coupons == null || coupons.isEmpty()) {
            Toast.makeText(context, "暂时没有可以使用的优惠券", Toast.LENGTH_SHORT).show();
            return;
        }
        new MaterialDialog.Builder(context)
                .title("选择优惠券(单选)")
                .negativeText("不使用优惠券")
                .items(coupons)
                .itemsCallbackSingleChoice(selectedCoupon, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selectedCoupon = which;
                        double discountMoney = Double.parseDouble(coupons.get(selectedCoupon).couponMoney);
                        selectCoupon.setTitle("已选择" + coupons.get(selectedCoupon).detail());
                        //　选择优惠券后，订单应付金额减到优惠券的金额
                        updateShouldPayMoney(-discountMoney);
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // 选择优惠券后，选择不使用优惠券，订单应付金额应加上之前选中的优惠券的金额
                        if (selectedCoupon != -1) {
                            shouldPayMoney.setTitle("<font color=\"#f65600\">实际付款：￥" + money + "</font>");
                        }
                        selectedCoupon = -1;
                        selectCoupon.setTitle("您有" + coupons.size() + "张优惠券可用");
                    }
                })
                .build().show();
    }

    private void loadCoupons(final Drug drug) {
        api.coupons(CouponType.CAN_USE_NOW, Coupon.Scope.DRUG_ORDER, money).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons = response;
                    if (drug.getUser_coupon_id().equals("0")) {
                        selectCoupon.setTitle("您有" + coupons.size() + "张优惠券可用");
                    }
                } else {
                    coupons = null;
                }
            }
        });
    }

    private void updateShouldPayMoney(double discountMoney) {

        double moneyWithCoupon = money + discountMoney > 0 ? money + discountMoney : 0;
        shouldPayMoney.setTitle("<font color=\"#f65600\">实际付款：￥" + moneyWithCoupon + "</font>");
    }

    private String getCouponId() {
        if (selectedCoupon == -1) {
            return String.valueOf(-1);
        }
        return coupons.get(selectedCoupon).id;
    }
}
