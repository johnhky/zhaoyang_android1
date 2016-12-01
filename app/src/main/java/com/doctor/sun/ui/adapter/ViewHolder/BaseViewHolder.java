package com.doctor.sun.ui.adapter.ViewHolder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.BR;


/**
 * Created by rick on 10/20/15.
 */
public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private boolean isBinding = true;
    private T mBinding;

    public BaseViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }

    public void bindTo(Object obj) {
        isBinding = true;
        mBinding.setVariable(BR.data, obj);
        mBinding.setVariable(BR.vh, this);
        mBinding.executePendingBindings();
        isBinding = false;
    }

    public boolean isBinding() {
        return isBinding;
    }

    public boolean isEven() {
        return getAdapterPosition() % 2 == 0;
    }
}
