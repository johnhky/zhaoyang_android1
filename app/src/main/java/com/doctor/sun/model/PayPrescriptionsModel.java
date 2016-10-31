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
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.MedicineInfo;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
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
    private double money;

    private HashMap<String, String> extraField;

    public PayPrescriptionsModel() {
        api = Api.of(ProfileModule.class);
        payApi = Api.of(AppointmentModule.class);
    }


    public List<SortedItem> parseData(Drug response) {
        Address address = new Address();
        address.setName(response.getTo());
        address.setPhone(response.getPhone());
        address.setAddress(response.getAddress());
        final MedicineInfo medicineInfo = new MedicineInfo();
        medicineInfo.setOrderId(String.valueOf(response.getId()));
        // 药品信息分行显示
        StringBuilder sb = new StringBuilder();
        for (String medicine : response.getDrug()) {
            sb.append(medicine);
            sb.append("\n");
        }
        // 删掉最后一个换行符
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        medicineInfo.setMedicine(sb.toString());
        medicineInfo.setMedicinePrice(Double.parseDouble(response.drugMoney));
        Doctor doctor = response.getDoctor();
        DrugExtraFee extra = response.getExtraFee();
        boolean hasPay = response.getHasPay() == IntBoolean.TRUE;
        money = Double.parseDouble(response.getMoney());


        List<SortedItem> result = new ArrayList<>();

        extraField = DrugListFragment.getDrugExtraField();

        address.setItemId("address");
        address.setPosition(result.size());
        result.add(address);

        Description detailDescription = new Description(R.layout.item_description, "医生信息");
        detailDescription.setItemId("doctorInfo");
        detailDescription.setPosition(result.size());
        result.add(detailDescription);

        doctor.setItemLayoutId(R.layout.item_base_doctor_info);
        doctor.setItemId("doctor");
        doctor.setPosition(result.size());
        result.add(doctor);

        ModelUtils.insertDividerNoMargin(result);

        Description drugDetail = new Description(R.layout.item_description, "药品明细");
        drugDetail.setItemId("drugDetail");
        drugDetail.setPosition(result.size());
        result.add(drugDetail);

        medicineInfo.setItemId("medicineInfo");
        medicineInfo.setPosition(result.size());
        result.add(medicineInfo);
        if (!extra.extraFee.isEmpty() || !extra.commission.isEmpty()) {
            Description extraFee = new Description(R.layout.item_description, "其他收费");
            extraFee.setItemId("extraFee");
            extraFee.setPosition(result.size());
            result.add(extraFee);
            ModelUtils.insertSpace(result, R.layout.space_dp8);
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
            ModelUtils.insertSpace(result, R.layout.space_dp8);
        }

        if (!extra.discount.isEmpty()) {
            Description discount = new Description(R.layout.item_description, "寄药优惠");
            discount.setItemId("discount");
            discount.setPosition(result.size());
            result.add(discount);
            ModelUtils.insertSpace(result, R.layout.space_dp8);
            for (String s : extra.discount) {
                ItemTextInput2 itemTextInput2 = new ItemTextInput2(R.layout.item_r_grey_text, s, "");
                itemTextInput2.setTitleGravity(Gravity.START);
                itemTextInput2.setItemId(s);
                itemTextInput2.setPosition(result.size());
                result.add(itemTextInput2);
            }
            ModelUtils.insertSpace(result, R.layout.space_dp8);
        }

        Description total = new Description(R.layout.item_description, "订单总计");
        total.setItemId("total");
        total.setPosition(result.size());
        result.add(total);
        ModelUtils.insertSpace(result, R.layout.space_dp8);
        String totalMoneyString = "总计： <font color=\"#f65600\">￥" + money + "</font>";


        ItemTextInput2 totalFee = new ItemTextInput2(R.layout.item_total_money, totalMoneyString, "");
        totalFee.setTitleGravity(Gravity.START);
        totalFee.setItemId("totalFee");
        totalFee.setPosition(result.size());
        //当没有收取服务费的时候，就要显示优惠了服务费
        totalFee.setUserSelected(extra.commission.isEmpty());

        result.add(totalFee);
        ModelUtils.insertSpace(result, R.layout.space_dp8);

        if (hasPay) {
            Description needPay = new Description(R.layout.item_description, "实际付款");
            needPay.setItemId("needPay");
            needPay.setPosition(result.size());
            result.add(needPay);
            ModelUtils.insertSpace(result, R.layout.space_dp8);

            String needPayMoneyString = "实付： <font color=\"#f65600\">￥" + money + "</font>";
            ItemTextInput2 needPayMoney = new ItemTextInput2(R.layout.item_total_money, needPayMoneyString, "");
            needPayMoney.setTitleGravity(Gravity.START);
            needPayMoney.setItemId("needPayMoney");
            needPayMoney.setPosition(result.size());
            result.add(needPayMoney);
        }

        ModelUtils.insertSpace(result, R.layout.space_dp8);

        if (!hasPay) {
            Description couponDescription = new Description(R.layout.item_description, "优惠券");
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

                    confirmPay(view.getContext(), medicineInfo.getOrderId(), money);
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
                        selectCoupon.setTitle("已优惠" + coupons.get(selectedCoupon).couponMoney + "元");
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedCoupon = -1;
                        selectCoupon.setTitle("未使用优惠券");
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

        extraField.put(Constants.DRUG_ORDER_ID, orderId);

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
