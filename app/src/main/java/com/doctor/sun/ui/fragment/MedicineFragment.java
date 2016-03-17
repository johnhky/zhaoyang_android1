package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.PFragmentMedicineBinding;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import io.realm.Realm;

/**
 * Created by lucas on 1/16/16.
 */
public class MedicineFragment extends Fragment {
    private SimpleAdapter mAdapter;
    private PFragmentMedicineBinding binding;
    private PageCallback callback;

    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(getContext());
    }

    public PFragmentMedicineBinding getBinding() {
        return binding;
    }

    public PageCallback getCallback() {
        return callback;
    }

    public SimpleAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PFragmentMedicineBinding.inflate(inflater,container,false);
        binding.rvAnswer.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        binding.rvAnswer.setAdapter(mAdapter);
        callback = new PageCallback(mAdapter);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        return binding.getRoot();
    }

    protected void loadMore() {

    }

}
