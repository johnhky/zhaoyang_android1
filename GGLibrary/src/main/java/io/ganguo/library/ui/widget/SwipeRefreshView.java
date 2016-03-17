package io.ganguo.library.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import io.ganguo.library.R;
import io.ganguo.library.core.drawable.MaterialProgressDrawable;

/**
 * ListView 带loadMore的下拉控件
 * <p/>
 * Created by Tony on 4/1/15.
 */
public class SwipeRefreshView extends SwipeRefreshLayout {

    private OnRefreshListener mOnRefreshListener = null;
    private ListView mListView = null;
    private View mFooterView = null;
    private MaterialProgressDrawable mFooterProgress;
    private boolean mIsLoading = false;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);

        post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    /**
     * 初始化listView，从控件里面找到
     */
    private void init() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof ListView) {
                mListView = (ListView) view;
                break;
            }
        }
        initListView();
    }

    /**
     * 初始化listView
     */
    private void initListView() {
        if (mListView == null) return;
        // footer
        mFooterView = View.inflate(getContext(), R.layout.view_loading_more, null);
        android.widget.ImageView pull_to_refresh_progress = (android.widget.ImageView) mFooterView.findViewById(R.id.pull_to_refresh_progress);
        mFooterProgress = new MaterialProgressDrawable(getContext(), pull_to_refresh_progress);
        mFooterProgress.setAlpha(255);
        mFooterProgress.setBackgroundColor(Color.TRANSPARENT);
        pull_to_refresh_progress.setImageDrawable(mFooterProgress);
        // onItemClick bug
        // 通过抢掉点击来解决点LoadMore时也会执行OnItemClick
        mFooterView.setClickable(true);
        mFooterView.setOnClickListener(null);
        mListView.addFooterView(mFooterView);
        mListView.setFooterDividersEnabled(false);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mCurrentScrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //bug fix: listview was not clickable after scroll
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    view.invalidateViews();
                }

                mCurrentScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount == totalItemCount) {
                    mFooterView.setVisibility(View.GONE);
                    mListView.setFooterDividersEnabled(false);
                    return;
                }
                mListView.setFooterDividersEnabled(true);

                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                if (!isRefreshing() && !mIsLoading && loadMore && mCurrentScrollState != SCROLL_STATE_IDLE) {
                    setLoadMore(true);
                    onLoadMore();
                }
            }
        });
    }

    /**
     * 设置loadMore状态
     *
     * @param isLoadMore
     */
    public void setLoadMore(boolean isLoadMore) {
        mIsLoading = isLoadMore;

        if (mFooterView == null) return;
        if (isLoadMore) {
            mFooterView.setVisibility(View.VISIBLE);
            mFooterProgress.start();
            setEnabled(false);
        } else {
            mFooterView.setVisibility(View.GONE);
            mFooterProgress.stop();
            mListView.setFooterDividersEnabled(false);
            setEnabled(true);
        }
    }

    /**
     * 回调loadMore状态
     */
    public void onLoadMore() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onLoadMore();
        }
    }

    /**
     * 设置loadMore监听器
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        super.setOnRefreshListener(listener);
        mOnRefreshListener = listener;
    }

    /**
     * 设置刷新完毕
     */
    public void onRefreshComplete() {
        setLoadMore(false);
        setRefreshing(false);
    }

    /**
     * 设置loading颜色 代理
     *
     * @param colors
     */
    @Override
    public void setColorSchemeColors(final int... colors) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setColors(colors);
            }
        }, 1000);
    }

    /**
     * 设置loading颜色
     *
     * @param colors
     */
    private void setColors(int... colors) {
        super.setColorSchemeColors(colors);
        if (mFooterProgress != null) {
            mFooterProgress.setColorSchemeColors(colors);
        }
    }

    /**
     * 下拉 loadMore 刷新监听器
     */
    public interface OnRefreshListener extends SwipeRefreshLayout.OnRefreshListener {
        void onLoadMore();
    }
}
