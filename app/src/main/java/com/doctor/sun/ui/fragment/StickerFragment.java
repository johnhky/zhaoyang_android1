package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.emoji.StickerManager;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 7/4/2016.
 */
public class StickerFragment extends ListFragment {
    public static final String TAG = StickerFragment.class.getSimpleName();


    public static StickerFragment newInstance(int page) {
        StickerFragment fragment = new StickerFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, page);
        fragment.setArguments(args);
        return fragment;
    }


    public int getPage() {
        return getArguments().getInt(Constants.POSITION);
    }

    @Override
    protected void loadMore() {
        getAdapter().addAll(StickerManager.getInstance().emoticons(getPage()));
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        loadMore();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.onFinishLoadMore(true);
        adapter.mapLayout(R.layout.item_emoji, R.layout.item_sticker);
        return adapter;
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
    }
}
