package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ReserveTimeBinding;
import com.doctor.sun.entity.Time;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;

/**
 * Created by rick on 16/1/2016.
 */
public class PickTimeAdapter extends SimpleAdapter<Time, ReserveTimeBinding> {
    private final int TYPE;
    private final long dateTime;
    private int selectedItem = -1;
    private Predicate<Time> predicate;

    public PickTimeAdapter(Context context, int type, long dateTime) {
        super(context);
        this.dateTime = dateTime;
        TYPE = type;
        predicate = new Predicate<Time>() {
            @Override
            public boolean apply(Time input) {
                return input.getType() == TYPE;
            }
        };
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ReserveTimeBinding> vh, int position) {
        super.onBindViewBinding(vh, position);
        if (vh.getItemViewType() == R.layout.reserve_time) {
            int adapterPosition = vh.getAdapterPosition();
            Time time = get(adapterPosition);
            boolean isSelected = (selectedItem == adapterPosition);
            vh.getBinding().tvTime.setSelected(isSelected);
            boolean reserva = time.getReserva() == 1;
            boolean past = time.getHandler().isPast(dateTime);
            vh.getBinding().tvTime.setActivated(past || reserva);
        }
    }


    public void select(BaseViewHolder vh) {
        ReserveTimeBinding binding = (ReserveTimeBinding) vh.getBinding();
        if (!binding.tvTime.isActivated()) {
            selectedItem = vh.getAdapterPosition();
            notifyDataSetChanged();
        }
    }


    @Override
    public boolean add(Time time) {
        if (time.getType() != TYPE) {
            return false;
        }
        return super.add(time);
    }

    @Override
    public void add(int location, Time time) {
        if (time.getType() != TYPE) {
            return;
        }
        super.add(location, time);
    }

    @Override
    public boolean addAll(int location, Collection<? extends Time> collection) {

        return super.addAll(location, Collections2.filter(collection, predicate));
    }

    @Override
    public boolean addAll(Collection<? extends Time> collection) {
        return super.addAll(Collections2.filter(collection, predicate));
    }

    public String getSelectedTime() {
        if (selectedItem == -1) {
            return "";
        }
        return get(selectedItem).getFrom().substring(0, 5);
    }

    public Time getSelectedItem() {
        if (selectedItem == -1) {
            return null;
        }
        return get(selectedItem);
    }

    public String getTime() {
        return get(selectedItem).time();
    }
}
