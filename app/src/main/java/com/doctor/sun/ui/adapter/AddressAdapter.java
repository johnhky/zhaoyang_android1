package com.doctor.sun.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.entity.Address;
import com.doctor.sun.BR;
import java.util.List;

/**
 * Created by heky on 17/4/28.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private List<Address> myDatas;

    public AddressAdapter(List<Address> myDatas) {
        this.myDatas = myDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.p_item_addresslist, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Address address = myDatas.get(position);
        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public MyViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object object) {
            binding.setVariable(BR.data, object);
            binding.executePendingBindings();
        }
    }


}
