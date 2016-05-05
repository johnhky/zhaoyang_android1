package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.entity.ItemButton;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.activity.patient.handler.CancelHandler;
import com.doctor.sun.ui.activity.patient.handler.MainActivityHandler;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by rick on 25/1/2016.
 */
public class AppointmentTypeDialog extends ListDialog {

    private final MainActivityHandler handler;

    public AppointmentTypeDialog(Context context, MainActivityHandler handler) {
        super(context);
        this.handler = handler;
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        ViewGroup.LayoutParams layoutParams = getBinding().recyclerView.getLayoutParams();
        layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.dp_344);
        layoutParams.width= getContext().getResources().getDimensionPixelSize(R.dimen.dp_290);
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
        getAdapter().add(new LayoutId() {
            @Override
            public int getItemLayoutId() {
                return R.layout.header_appointment_type;
            }
        });

        getAdapter().add(new ItemButton(R.layout.item_blue_text, "我自己挑选医生") {
            @Override
            public void onClick(View view) {
                Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.DETAIL);
                view.getContext().startActivity(intent);
                dismiss();
            }
        });

        getAdapter().add(new ItemButton(R.layout.item_blue_text, "让医生抢单") {
            @Override
            public void onClick(View view) {
                handler.showWarning(view);
                dismiss();
            }
        });

        getAdapter().add(new CancelHandler(getOwnerActivity(), this));
        getAdapter().onFinishLoadMore(true);
        getAdapter().notifyDataSetChanged();
    }

}
