package com.doctor.sun.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentRefreshListBinding;
import com.doctor.sun.event.HideFABEvent;
import com.doctor.sun.event.ShowFABEvent;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import io.realm.Realm;

/**
 * Created by Lynn on 2/22/16.
 */
public class RefreshListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    protected FragmentRefreshListBinding binding;
    private SimpleAdapter mAdapter;
    public Realm realm;
    private PageCallback<T> pageCallback;
    private boolean isLoading = false;
    private int mActionBarAutoHideSignal = 0;
    private boolean isFirstTime = true;

    public RefreshListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refresh_list, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isFirstTime) {
                    isFirstTime = false;
                    return;
                }

                if (shouldShowFAB(dy)) {
                    EventHub.post(getShowFABEvent());
                } else {
                    EventHub.post(getHideFABEvent());
                }
            }
        });
        mAdapter = createAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                if (!isLoading) {
                    loadMore();
                    isLoading = true;
                }
            }

            @Override
            protected void onFinishLoadMore() {
                super.onFinishLoadMore();
                isLoading = false;
                refreshEmptyIndicator();
            }
        });
        binding.recyclerView.setAdapter(mAdapter);
        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.swipeRefresh.setOnRefreshListener(this);

        getPageCallback();
        return binding.getRoot();
    }

    private boolean shouldShowFAB(int deltaY) {
        int mActionBarAutoHideSensivity = 250;
        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        return (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
    }

    public ShowFABEvent getShowFABEvent() {
        return new ShowFABEvent();
    }

    public HideFABEvent getHideFABEvent() {
        return new HideFABEvent();
    }

    protected void refreshEmptyIndicator() {
        if (mAdapter != null && !mAdapter.isEmpty()) {
            binding.emptyIndicator.setText(getEmptyIndicatorText());
            binding.emptyIndicator.setVisibility(View.GONE);
        } else {
            binding.emptyIndicator.setText(getEmptyIndicatorText());
            binding.emptyIndicator.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    protected String getEmptyIndicatorText() {
        return "";
    }

    public PageCallback getPageCallback() {
        if (pageCallback == null) {
            pageCallback = new PageCallback<T>(mAdapter) {
                @Override
                public void onInitHeader() {
                    insertHeader();
                }

                @Override
                public void onFinishRefresh() {
                    super.onFinishRefresh();
                    Tasks.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    }, 1000);
                }

                @Override
                public void insertFooter() {
                    super.insertFooter();
                    RefreshListFragment.this.insertFooter();
                }
            };
        }
        if (pageCallback.getAdapter() == null) {
            pageCallback.setAdapter(getAdapter());
        }
        return pageCallback;
    }

    protected void insertHeader() {

    }

    protected void insertFooter() {

    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter();
    }

    @CallSuper
    protected void loadMore() {
        binding.swipeRefresh.setRefreshing(true);
    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    public FragmentRefreshListBinding getBinding() {
        return binding;
    }

    @Override
    public void onRefresh() {
        isLoading = true;
        getPageCallback().resetPage();
        binding.swipeRefresh.setRefreshing(true);
        loadMore();
    }
}
