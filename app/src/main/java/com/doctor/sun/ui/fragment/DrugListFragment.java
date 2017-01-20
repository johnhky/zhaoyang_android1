package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.HashMap;

/**
 * Created by rick on 1/3/2016.
 */
@Factory(type = BaseFragment.class, id = "DrugListFragment")
public class DrugListFragment extends RefreshListFragment {
    public static final String TAG = DrugListFragment.class.getSimpleName();

    public static final String COUPON_ID = "couponId";
    private DrugModule api = Api.of(DrugModule.class);
    private MultiSelectAdapter adapter;
    private static HashMap<String, String> drugExtraField = new HashMap<>();

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);

        return bundle;
    }

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
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new MultiSelectAdapter();
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.orderList(getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @Override
    protected void refreshEmptyIndicator() {
        if (getAdapter() != null && !getAdapter().isEmpty()) {
            String s = getEmptyIndicatorText();
            SpannableString spanned = new SpannableString(s);
            spanned.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = MedicineStoreActivity.makeIntent(getContext());
                    getContext().startActivity(intent);
                }
            }, s.length() - 6, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.emptyIndicator.setText(spanned);
            binding.emptyIndicator.setMovementMethod(LinkMovementMethod.getInstance());
            binding.emptyIndicator.setVisibility(View.GONE);
        } else {
            String s = getEmptyIndicatorText();
            SpannableString spanned = new SpannableString(s);
            spanned.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = MedicineStoreActivity.makeIntent(getContext());
                    getContext().startActivity(intent);
                }
            }, s.length() - 6, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            binding.emptyIndicator.setText(spanned);
            binding.emptyIndicator.setMovementMethod(LinkMovementMethod.getInstance());
            binding.emptyIndicator.setVisibility(View.VISIBLE);
        }


    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "您当前暂无寄药订单" +
                "\n如需使用寄药服务请联系寄药小助手" +
                "\n找寄药小助手";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_medicinestore, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_medicine_store: {
                Intent intent = MedicineStoreActivity.makeIntent(getContext());
                getContext().startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
