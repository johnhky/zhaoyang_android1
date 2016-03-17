package com.doctor.sun.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.entity.ItemButton;
import com.doctor.sun.ui.activity.patient.handler.CancelHandler;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.handler.AppointmentHandler;
import com.doctor.sun.util.PayInterface;

/**
 * Created by rick on 25/1/2016.
 */
public class PayMethodDialog extends ListDialog {

    private final PayInterface handler;
    private final Activity activity;

    public PayMethodDialog(Context context, PayInterface handler) {
        super(context);
        this.handler = handler;
        activity = (Activity) context;
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        ViewGroup.LayoutParams layoutParams = getBinding().recyclerView.getLayoutParams();
        int itemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dp_59);
        if (BuildConfig.DEV_MODE) {
            layoutParams.height = itemHeight * 4;
        } else {
            layoutParams.height = itemHeight * 3;
        }

        getBinding().recyclerView.setLayoutParams(layoutParams);
    }

    @Override
    protected void initAdapter() {
        SimpleAdapter adapter = new SimpleAdapter(getContext());
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        setAdapter(adapter);
    }

    @Override
    protected void loadMore() {
        getAdapter().add(new ItemButton(R.layout.item_pay_method, "支付宝") {
            @Override
            public void onClick(View view) {
                handler.payWithAlipay(activity);
                dismiss();
            }
        });

        getAdapter().add(new ItemButton(R.layout.item_pay_method, "微信支付") {
            @Override
            public void onClick(View view) {
                handler.payWithWeChat(activity);
                dismiss();
            }
        });
        if (BuildConfig.DEV_MODE) {
            getAdapter().add(new ItemButton(R.layout.item_pay_method, "模拟支付") {
                @Override
                public void onClick(View view) {
                    handler.simulatedPay(null, view, null);
                    dismiss();
                }
            });
        }

        getAdapter().add(new CancelHandler(getOwnerActivity(), this));
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }

}
