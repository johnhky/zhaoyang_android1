package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 1/3/2016.
 */
public class DrugListFragment extends RefreshListFragment {
    public static final String COUPON_ID = "couponId";
    private DrugModule api = Api.of(DrugModule.class);
    public boolean isVisibleToUser = false;
    public boolean useCoupon = false;
    private Snackbar snackbar;
    private MultiSelectAdapter adapter;
    ProfileModule profileModule = Api.of(ProfileModule.class);
    private List<Coupon> coupons = new ArrayList<>();
    private static HashMap<String, String> drugExtraField = new HashMap<>();

    public static DrugListFragment getInstance() {
        return new DrugListFragment();
    }

    public static HashMap<String, String> getDrugExtraField() {
        drugExtraField.put("body", "drug order");
        return drugExtraField;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileModule.coupons(CouponType.CAN_USE_NOW).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons.addAll(response);
                    showSnackBar();
                }
            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new MultiSelectAdapter(getContext());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showSnackBar();
            }
        });
        return adapter;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (binding != null) {
                showSnackBar();
            }
        } else {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showSnackBar() {
        if (!isVisibleToUser) {
            return;
        }
        if (coupons.isEmpty()) {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            return;
        }
        if (adapter != null && adapter.isEmpty()) {
            return;
        }
        if (!useCoupon) {
            String format = "您有一张100元优惠券可以使用";
            snackbar = Snackbar.make(binding.getRoot(), format, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("使用优惠券", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    useCoupon = true;
                    adapter.selectAll();
                    drugExtraField.put(COUPON_ID, coupons.get(0).getId());
                    getAdapter().notifyDataSetChanged();
                }
            });
            snackbar.show();
        } else {
            snackbar = Snackbar.make(binding.getRoot(), "您将使用优惠券进行寄药订单支付", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("取消使用优惠券", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    useCoupon = false;
                    adapter.unSelectAll();
                    drugExtraField.remove(COUPON_ID);
                    getAdapter().notifyDataSetChanged();
                }
            });
            snackbar.show();
        }
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.orderList(getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "没有任何寄药订单";
    }
}
