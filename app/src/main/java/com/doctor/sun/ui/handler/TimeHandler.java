package com.doctor.sun.ui.handler;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.doctor.sun.R;
import com.doctor.sun.dto.AllDateDTO;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.doctor.AddDisturbActivity;
import com.doctor.sun.ui.activity.doctor.AddTimeActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

/**
 * Created by lucas on 12/9/15.
 */
public class TimeHandler {

    private AllDateDTO dto;
    private Time data;
    private int mhour;
    private int mminute;
    private boolean isEditMode;
    private GetIsEditMode editStatus;

    public TimeHandler(Time time) {
        data = time;
    }

    public TimeHandler(){

    }

    public interface GetIsEditMode {
        boolean getIsEditMode();
    }

    public void addTime(View view) {
        Intent intent = AddTimeActivity.makeIntent(view.getContext(), null);
        view.getContext().startActivity(intent);
    }

    public void addDisturb(View view) {
        Intent intent = AddDisturbActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void upDateTime(View view) {
        Intent intent = AddTimeActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public OnItemClickListener deleteDialog() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                final TimeModule api = Api.of(TimeModule.class);
                String questiion = "确定删除该出诊时间？";
                String cancel = "取消";
                String delete = "删除";
                TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), questiion, cancel, delete, new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(final TwoSelectorDialog deleteDialog) {
                        api.deleteTime(data.getId()).enqueue(new ApiCallback<String>() {
                            @Override
                            protected void handleResponse(String response) {

                            }

                            @Override
                            protected void handleApi(ApiDTO<String> body) {
                                deleteDialog.dismiss();
                                adapter.remove(data);
                                adapter.notifyItemRemoved(vh.getAdapterPosition());

                                LayoutId objectFace = (LayoutId) adapter.get(adapter.size() - 1);
                                if (objectFace.getItemLayoutId() == R.layout.item_time_category) {
                                    adapter.remove(adapter.size() - 1);
                                    adapter.notifyItemRemoved(adapter.size() - 1);
                                }
                                if (adapter.size() > 2) {
                                    LayoutId objectNetwork = (LayoutId) adapter.get(1);
                                    if (objectNetwork.getItemLayoutId() == R.layout.item_time_category) {
                                        adapter.remove(0);
                                        adapter.notifyItemRemoved(0);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    public OnItemClickListener deleteDisturb() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                final TimeModule api = Api.of(TimeModule.class);
                TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), "确定删除该免打扰时间？", "取消", "删除", new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                        api.deleteTime(data.getId()).enqueue(new ApiCallback<String>() {
                            @Override
                            protected void handleResponse(String response) {

                            }

                            @Override
                            protected void handleApi(ApiDTO<String> body) {
                                dialog.dismiss();
                                adapter.remove(data);
                                adapter.notifyItemRemoved(vh.getAdapterPosition());
                            }
                        });

                    }

                    @Override
                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }


    public void beginTimeSet(View view) {
        final TextView tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mhour = hourOfDay;
                mminute = minute;
                tvBeginTime.setText(String.format("%02d:%02d", mhour, mminute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), onTimeSetListener, mhour, mminute, true);
        timePickerDialog.show();
    }


    public void endTimeSet(View view) {
        final TextView tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mhour = hourOfDay;
                mminute = minute;
                tvEndTime.setText(String.format("%02d:%02d", mhour, mminute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), onTimeSetListener, mhour, mminute, true);
        timePickerDialog.show();
    }


    public void select(View view) {
        view.setSelected(!view.isSelected());
    }
}
