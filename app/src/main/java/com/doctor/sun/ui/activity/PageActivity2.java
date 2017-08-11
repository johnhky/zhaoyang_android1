package com.doctor.sun.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityListBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.vm.ItemSearch;
import com.google.common.base.Strings;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 20/1/2016.
 */
public class PageActivity2 extends BaseFragmentActivity2 implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    protected String keyword = "";
    private ActivityListBinding binding;
    private SimpleAdapter adapter;
    private PageCallback<Object> callback;
    private ItemSearch searchItem;
    private boolean hasSearchItem = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        initHeader();
        initAdapter();
        initRecyclerView();
        initRefreshLayout();
        getDataFrom();
    }
    public void getDataFrom(){}
    private void initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
    }

    protected void initHeader() {
    }

    protected void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

    }

    protected void initAdapter() {
        adapter = createAdapter();
        callback = createCallback();
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });

    }

    @NonNull
    private PageCallback<Object> createCallback() {
        return new PageCallback<Object>(adapter) {
            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
                if (searchItem != null) {
                    if (!Strings.isNullOrEmpty(keyword)) {
                        EditText viewById = (EditText) binding.recyclerView.findViewById(R.id.search);
                        searchItem.editKeyword(viewById);
                    }
                }
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hasNoContent()) {
                            binding.emptyIndicator.setText(getEmptyIndicatorText());
                            binding.emptyIndicator.setVisibility(View.VISIBLE);
                        } else {
                            binding.emptyIndicator.setVisibility(View.GONE);
                        }
                    }
                }, 1000);
            }

            @Override
            public void onInitHeader() {
                super.onInitHeader();
                onPrepareHeader();
            }
        };
    }

    public boolean hasNoContent() {
        if (hasSearchItem) {
            return adapter != null && adapter.size() <= 1;
        }
        return adapter != null && adapter.isEmpty();
    }

    protected void onPrepareHeader() {

    }

    @NonNull
    public String getEmptyIndicatorText() {
        return "";
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter();
    }

    @CallSuper
    protected void loadMore() {
        binding.refreshLayout.setRefreshing(true);
    }


    public ActivityListBinding getBinding() {
        return binding;
    }

    public SimpleAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SimpleAdapter adapter) {
        this.adapter = adapter;
    }

    public PageCallback getCallback() {
        if (callback.getAdapter() == null) {
            callback.setAdapter(getAdapter());
        }
        return callback;
    }

    public void setCallback(PageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onRefresh() {
        callback.resetPage();
        adapter.clear();
        adapter.onFinishLoadMore(true);
        adapter.notifyDataSetChanged();
        loadMore();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    public void insertSearchItem() {
        if (searchItem == null) {
            hasSearchItem = true;
            searchItem = new ItemSearch();
            searchItem.setItemLayoutId(R.layout.item_search);
            searchItem.setItemId("id");
            searchItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    if (i == BR.keyword) {
                        if (Strings.isNullOrEmpty(searchItem.getKeyword())) {
                            if (Strings.isNullOrEmpty(keyword)) {
                                return;
                            }
                            keyword = searchItem.getKeyword();
                            getCallback().resetPage();
                            loadMore();
                        }
                    } else if (i == BR.submit) {
                        keyword = searchItem.getKeyword();
                        getCallback().resetPage();
                        loadMore();
                    }
                }
            });
        }
        getAdapter().add(searchItem);
    }

}

