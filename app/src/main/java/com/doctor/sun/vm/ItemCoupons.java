package com.doctor.sun.vm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.module.ProfileModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 20/12/2016.
 */

public class ItemCoupons extends BaseItem {
    private ProfileModule api = Api.of(ProfileModule.class);
    ;
    private List<Coupon> coupons = new ArrayList<>();
    private int selectedCoupon = -1;
    private Drug drug;

    public ItemCoupons(Drug drug) {
        this.drug = drug;
        if (notPay()) {
            loadCoupons();
        }
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
        notifyChange();
    }

    public boolean canUseCoupon() {
        return drug.getUser_coupon_id().equals("0");
    }

    private void loadCoupons() {
        api.coupons(CouponType.CAN_USE_NOW, Coupon.Scope.DRUG_ORDER, drug.getNeed_pay()).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    setCoupons(response);
                }
            }
        });
    }


    public void selectCoupon(Context context) {
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
                        notifyChange();
                        return true;
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedCoupon = -1;
                        notifyChange();
                    }
                })
                .build().show();
    }

    public int getSelectedCoupon() {
        return selectedCoupon;
    }

    public String getCouponId() {
        if (selectedCoupon == -1) {
            return String.valueOf(-1);
        }
        return coupons.get(selectedCoupon).id;
    }


    @Override
    public String getTitle() {
        if (!canUseCoupon()) {
            if (notPay()) {
                return "已选取" + drug.getCoupon_info().couponMoney + "元优惠券";
            }else {
                return "已使用" + drug.getCoupon_info().couponMoney + "元优惠券";
            }
        }
        if (!notPay()) {
            return "未使用优惠券";
        }
        if (coupons != null && coupons.size() > 0) {
            if (selectedCoupon != -1) {
                return "已选择" + coupons.get(selectedCoupon).detail();
            } else {
                return "您有" + coupons.size() + "张优惠券可用";
            }
        }
        return "暂时没有可以选择的优惠券";
    }

    public boolean notPay() {
        return drug.getHas_pay() == IntBoolean.FALSE;
    }

    public boolean neverUseCouponBefore() {
        return drug.getCoupon_info() == null
                && drug.getCoupon_info().couponMoney == null;
    }

    public double getDiscountMoney() {
        if (canUseCoupon()) {
            if (selectedCoupon == -1) {
                return 0D;
            }
            double discount = Double.parseDouble(coupons.get(selectedCoupon).couponMoney);
            return Math.max(0D, discount);
        } else {
            return 0D;
        }
    }
}
