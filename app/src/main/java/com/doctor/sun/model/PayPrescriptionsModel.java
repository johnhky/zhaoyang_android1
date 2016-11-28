package com.doctor.sun.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ProfileModule;
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
    private AppointmentModule payApi;

    private List<Coupon> coupons;
    private int selectedCoupon = -1;

    private ClickMenu selectCoupon;
    private ItemRadioGroup payMethod;
    private ItemTextInput2 shouldPayMoney;
    private double money;

    private HashMap<String, String> extraField;

    public PayPrescriptionsModel() {
        api = Api.of(ProfileModule.class);
        payApi = Api.of(AppointmentModule.class);
    }


    public List<SortedItem> parseData(final Drug response) {

        DrugExtraFee extra = response.getExtra_fee();
        boolean hasPay = response.getHas_pay() == IntBoolean.TRUE;
        money = response.getMoney();

        List<SortedItem> result = new ArrayList<>();

        extraField = DrugListFragment.getDrugExtraField();

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        ItemTextInput2 receiver = new ItemTextInput2(R.layout.item_text_h2, "收件人：" + response.getTo(), "");
        receiver.setItemId("receiver");
        receiver.setPosition(result.size());
        result.add(receiver);

        ItemTextInput2 phone = new ItemTextInput2(R.layout.item_text_h2, "手机号码：" + response.getPhone(), "");
        phone.setItemId("phone");
        phone.setPosition(result.size());
        result.add(phone);

        ItemTextInput2 address = new ItemTextInput2(R.layout.item_text_h2, "收货地址：" + response.getAddress(), "");
        address.setItemId("address");
        address.setPosition(result.size());
        result.add(address);

        ItemTextInput2 remark = new ItemTextInput2(R.layout.item_text_h2, "备注信息：" + response.getRemark(), "");
        remark.setItemId("remark");
        remark.setPosition(result.size());
        result.add(remark);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        Description drugDetail = new Description(R.layout.item_description, "寄药详细");
        drugDetail.setItemId("drugDetail");
        drugDetail.setPosition(result.size());
        result.add(drugDetail);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        ItemTextInput2 recordName = new ItemTextInput2(R.layout.item_text_h2, "处方病历：" + response.getRecord_name(), "");
        recordName.setItemId("recordName");
        recordName.setPosition(result.size());
        result.add(recordName);

        ItemTextInput2 doctorName = new ItemTextInput2(R.layout.item_text_h2, "处方医生：" + response.getDoctor().getName(), "");
        doctorName.setItemId("doctorName");
        doctorName.setPosition(result.size());
        result.add(doctorName);

        ItemTextInput2 createdTime = new ItemTextInput2(R.layout.item_text_h2, "下单时间：" + response.getCreated_at(), "");
        createdTime.setItemId("createdTime");
        createdTime.setPosition(result.size());
        result.add(createdTime);

        ModelUtils.insertSpace(result, R.layout.space_8dp);
        ModelUtils.insertDividerNoMargin(result);

        if (!response.getDrug().isEmpty()) {
            ModelUtils.insertSpace(result, R.layout.space_8dp);

            ItemTextInput2 itemText = new ItemTextInput2(R.layout.item_text_h2, "药品品类：", "");
            itemText.setItemId("药品品类");
            itemText.setPosition(result.size());
            result.add(itemText);

            ModelUtils.insertSpace(result, R.layout.space_8dp);

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
            Description couponDescription = new Description(R.layout.item_description, "优惠券");
            couponDescription.setBackgroundColor(R.color.color_coupon_background_yellow);
            couponDescription.setTitleColor(R.color.white);
            couponDescription.setItemId("couponDescription");
            couponDescription.setPosition(result.size());
            result.add(couponDescription);


            extraField = DrugListFragment.getDrugExtraField();

            selectCoupon = new ClickMenu(R.layout.item_select_coupon, 0, "未使用优惠券", new View.OnClickListener() {
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

        Description total = new Description(R.layout.item_description, "总价");
        total.setItemId("total");
        total.setPosition(result.size());
        result.add(total);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        String totalMoneyString = "订单应付：￥" + money;
        if (extra.commission.isEmpty()) {
            totalMoneyString += "（服务费: ￥100）";
        }
        ItemTextInput2 totalMoney = new ItemTextInput2(R.layout.item_r_grey_text, totalMoneyString, "");
        totalMoney.setTextSize(R.dimen.font_12);
        totalMoney.setTitleGravity(Gravity.START);
        totalMoney.setItemId("totalMoney");
        totalMoney.setPosition(result.size());
        result.add(totalMoney);

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        String shouldPayMoneyString = "<font color=\"#f65600\">实际付款：￥" + money + "</font>";
        shouldPayMoney = new ItemTextInput2(R.layout.item_total_money, shouldPayMoneyString, "");
        shouldPayMoney.setTitleGravity(Gravity.START);
        shouldPayMoney.setItemId("shouldPayMoney");
        shouldPayMoney.setPosition(result.size());
        //当没有收取服务费的时候，就要显示优惠了服务费
        shouldPayMoney.setUserSelected(extra.commission.isEmpty());

        result.add(shouldPayMoney);
        ModelUtils.insertSpace(result, R.layout.space_8dp);

        if (hasPay) {
            Description needPay = new Description(R.layout.item_description, "实际付款");
            needPay.setItemId("needPay");
            needPay.setPosition(result.size());
            result.add(needPay);
            ModelUtils.insertSpace(result, R.layout.space_8dp);

            String needPayMoneyString = "实付： <font color=\"#f65600\">￥" + money + "</font>";
            ItemTextInput2 needPayMoney = new ItemTextInput2(R.layout.item_total_money, needPayMoneyString, "");
            needPayMoney.setTitleGravity(Gravity.START);
            needPayMoney.setItemId("needPayMoney");
            needPayMoney.setPosition(result.size());
            result.add(needPayMoney);
        }

        ModelUtils.insertSpace(result, R.layout.space_8dp);

        if (!hasPay) {

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

                    confirmPay(view.getContext(), response.getId(), money);
                }
            };
            confirmButton.setItemId("confirmButton");
            confirmButton.setPosition(result.size());
            result.add(confirmButton);

            loadCoupons();
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
                        selectCoupon.setTitle("已优惠" + discountMoney + "元");
                        updateShouldPayMoney(discountMoney);
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedCoupon = -1;
                        selectCoupon.setTitle("未使用优惠券");
                        updateShouldPayMoney(0);
                    }
                })
                .build().show();
    }

    private void loadCoupons() {
        api.coupons(CouponType.CAN_USE_NOW, Coupon.Scope.DRUG_ORDER, money).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons = response;
                } else {
                    coupons = null;
                }
            }
        });
    }

    private void updateShouldPayMoney(double discountMoney) {

        double shouldPay = money - discountMoney > 0 ? money - discountMoney : 0;
        shouldPayMoney.setTitle("<font color=\"#f65600\">实际付款：￥" + shouldPay + "</font>");
    }

    private String getCouponId() {
        if (selectedCoupon == -1) {
            return String.valueOf(-1);
        }
        return coupons.get(selectedCoupon).id;
    }

    private void confirmPay(Context context, String orderId, double totalFee) {
        if (payMethod.getSelectedItem() == -1) {
            Toast.makeText(context, "请选择支付方式", Toast.LENGTH_SHORT).show();
            return;
        }

        extraField.put("drugOrderId", orderId);

        if (!getCouponId().equals("-1")) {
            extraField.put("couponId", getCouponId());
        }

        switch (payMethod.getSelectedItem()) {
            case PayMethod.ALIPAY:
                payApi.buildAlipayGoodsOrder(totalFee, "alipay", extraField)
                        .enqueue(new AlipayCallback((Activity) context, totalFee, extraField));
                break;
            case PayMethod.WECHAT:
                payApi.buildWeChatGoodsOrder(totalFee, "wechat", extraField)
                        .enqueue(new WeChatPayCallback((Activity) context, totalFee, extraField));
                break;
            case PayMethod.SIMULATED:
                Toast.makeText(context, "模拟支付", Toast.LENGTH_SHORT).show();
        }
    }
}
