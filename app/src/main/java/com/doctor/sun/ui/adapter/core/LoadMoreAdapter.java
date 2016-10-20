package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.IncludeLoadingBinding;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

import io.ganguo.library.core.drawable.MaterialProgressDrawable;

/**
 * Created by rick on 10/23/15.
 */
public abstract class LoadMoreAdapter<T, VH extends ViewDataBinding> extends BaseAdapter<T, VH> {
//    private LoadingView loadingView;

    private boolean isLoading = false;
    private LoadMoreListener mLoadMoreListener;
    private boolean isLastPage = false;

    public LoadMoreAdapter(Context context) {
        super(context);
    }


    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.include_loading) {
            IncludeLoadingBinding binding = IncludeLoadingBinding.inflate(getInflater(), parent, false);
            return new BaseViewHolder<>(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.include_loading) {
            if (!isLastPage) {
                loadMore();
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (isLastPage) {
            return size();
        }
        return size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isLastPage) {
            if (position == getItemCount() - 1) {
                return R.layout.include_loading;
            }
        }
        return super.getItemViewType(position);
    }

    public void onFinishLoadMore(boolean lastPage) {
        isLastPage = lastPage;
        isLoading = false;
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onFinishLoadMore();
        }
    }

    public void loadMore() {
        if (isLoading) {
            return;
        }
        if (mLoadMoreListener != null) {
            isLoading = true;
            mLoadMoreListener.onLoadMore();
        }
    }


}
