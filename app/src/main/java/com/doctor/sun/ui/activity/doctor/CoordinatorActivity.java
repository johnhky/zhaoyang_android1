package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityCoordinatorBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

/**
 * Created by rick on 15/2/2017.
 */
public abstract class CoordinatorActivity extends BaseFragmentActivity2 {

    private ActivityCoordinatorBinding binding;
    private SortedListAdapter adapter;
    private boolean isToolbarCollapsed = false;

    public static Intent intentFor(Context context) {
        return new Intent(context, MyIncomeActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coordinator);
        postponeTransition();
        initToolbar();
        initHeader(binding.flyHeader);
        initAdapter();
        initRecyclerView();

        loadMore();
    }

    protected abstract void loadMore();

    protected abstract void initHeader(FrameLayout flyHeader);

    private void initAdapter() {
        adapter = createAdapter();
    }

    final protected SortedListAdapter getAdapter() {
        return adapter;
    }

    private SortedListAdapter createAdapter() {
        return new SortedListAdapter();
    }

    private void initRecyclerView() {
        binding.rv.setLayoutManager(createLayoutManager());
        binding.rv.setAdapter(adapter);
    }

    @NonNull
    private LinearLayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    private void postponeTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        } else {
            supportPostponeEnterTransition();
        }
    }

    private void startPostponedTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        } else {
            supportStartPostponedEnterTransition();
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolBar);
        // 上滑时，toolbar改变颜色
        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isToolbarCollapsed = true;
                    binding.toolBar.setNavigationIcon(R.drawable.ic_back_white);
                } else {
                    isToolbarCollapsed = false;
                    binding.toolBar.setNavigationIcon(R.drawable.ic_back_white);
                }
                invalidateOptionsMenu();
            }
        });
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
        binding.tbTitle.setText("我的账单");
    }

    public ActivityCoordinatorBinding getBinding() {
        return binding;
    }
}
