package com.doctor.sun.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogFragmentListBinding;
import com.doctor.sun.databinding.FragmentList2Binding;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by Lynn on 2/22/16.
 */
public class BottomSheetListFragment<T> extends BottomSheetDialogFragment {
    protected DialogFragmentListBinding binding;
    private SimpleAdapter mAdapter;
    public Realm realm;
    private boolean isLoading = false;

    public BottomSheetListFragment() {
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_list, container, false);
        binding.tbTitle.setText("历史记录");
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
            }
        });
        binding.recyclerView.setAdapter(mAdapter);
        return binding.getRoot();
    }


    @NonNull
    protected String getEmptyIndicatorText() {
        return "";
    }

    @NonNull
    public SimpleAdapter createAdapter() {
        return new SimpleAdapter();
    }

    @CallSuper
    protected void loadMore() {
    }

    public SimpleAdapter getAdapter() {
        return mAdapter;
    }

    public DialogFragmentListBinding getBinding() {
        return binding;
    }


}
