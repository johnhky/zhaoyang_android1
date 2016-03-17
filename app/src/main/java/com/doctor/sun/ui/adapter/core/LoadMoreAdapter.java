package com.doctor.sun.ui.adapter.core;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.IncludeLoadingBinding;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

import io.ganguo.library.core.drawable.MaterialProgressDrawable;

/**
 * Created by rick on 10/23/15.
 */
public abstract class LoadMoreAdapter<T, VH extends ViewDataBinding> extends BaseAdapter<T, VH> {
    private LoadingView loadingView;

    private LoadMoreListener mLoadMoreListener;
    private boolean isLastPage = false;

    public LoadMoreAdapter(Context context) {
        super(context);
    }

    public LoadMoreListener getLoadMoreListener() {
        return mLoadMoreListener;
    }

    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.include_loading) {
            IncludeLoadingBinding binding = IncludeLoadingBinding.inflate(getInflater(), parent, false);
            loadingView = new LoadingView(getContext(), binding);
            return new BaseViewHolder<>(binding);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() == R.layout.include_loading) {
            if (!isLastPage) {
                IncludeLoadingBinding binding = (IncludeLoadingBinding) holder.getBinding();
                binding.imageView.setVisibility(View.VISIBLE);
                loadingView.start();
                loadMore();
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (isLastPage){
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

    public void hideLoadMore() {
        isLastPage = true;
        if (loadingView != null) {
            loadingView.stop();
            loadingView = null;
        }
        notifyItemRemoved(getItemCount());
    }

    public void onFinishLoadMore(boolean lastPage) {
        isLastPage = lastPage;
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    public void loadMore() {
        if (mLoadMoreListener != null) {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoadMoreListener.onLoadMore();
                }
            }, 500);
        }
    }


    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<VH> holder) {
        if (loadingView == null) {
            return;
        }
        if (holder.getItemViewType() == R.layout.include_loading) {
            loadingView.stop();
        }
        super.onViewDetachedFromWindow(holder);
    }

    private class LoadingView {
        private MaterialProgressDrawable mFooterProgress;
        private Context context;
        private IncludeLoadingBinding binding;
        private boolean isFinish = false;

        public LoadingView(Context context, IncludeLoadingBinding binding) {
            this.context = context;
            this.binding = binding;
        }

        private Context getContext() {
            return context;
        }

        private void initProgressView() {
            mFooterProgress = new MaterialProgressDrawable(getContext(), this.binding.getRoot());
            mFooterProgress.setAlpha(255);
            mFooterProgress.setBackgroundColor(Color.TRANSPARENT);
            Resources resources = getContext().getResources();
            int color = resources.getColor(R.color.colorPrimaryDark);
            int blue = resources.getColor(R.color.colorPrimaryDark);
            int green = resources.getColor(R.color.colorPrimaryDark);
            mFooterProgress.setColorSchemeColors(color, blue, green);
        }

        public void start() {
            if (!isFinish) {
                if (mFooterProgress == null) {
                    initProgressView();
                }

                if (!mFooterProgress.isRunning()) {
                    binding.imageView.setImageDrawable(mFooterProgress);
                    mFooterProgress.start();
                }
            }
        }

        public void stop() {
            if (binding.getRoot() != null) {
                if (mFooterProgress != null) {
                    mFooterProgress.stop();
                    binding.imageView.setImageDrawable(null);
                }
            }
        }

    }
}
