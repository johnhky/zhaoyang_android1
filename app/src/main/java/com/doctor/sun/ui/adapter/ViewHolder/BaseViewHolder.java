package com.doctor.sun.ui.adapter.ViewHolder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.BR;


/**
 * Created by rick on 10/20/15.
 */
public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private T mBinding;

    public BaseViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }

    public void bindTo(Object obj) {
        mBinding.setVariable(BR.data, obj);
        mBinding.setVariable(BR.vh, this);
        mBinding.executePendingBindings();
    }

}
