package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.emoji.EmojiManager;
import com.doctor.sun.emoji.Emoticon;
import com.doctor.sun.ui.adapter.SimpleAdapter;

/**
 * Created by rick on 7/4/2016.
 */
public class EmoticonFragment extends ListFragment {
    public static final String TAG = EmoticonFragment.class.getSimpleName();
    public static final int PER_PAGE = 20;


    public static EmoticonFragment newInstance(int page) {
        EmoticonFragment fragment = new EmoticonFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, page);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    protected void loadMore() {

        int position = getArguments().getInt(Constants.POSITION);

        int startPosition = PER_PAGE * position;
        int endPosition = PER_PAGE * (position + 1);
        if (endPosition > EmojiManager.getDisplayCount()) {
            endPosition = EmojiManager.getDisplayCount();
        }
        for (int i = startPosition; i < endPosition; i++) {
            Emoticon emoticon = new Emoticon();
            emoticon.setAssetPath(EmojiManager.getAssetPath(i));
            emoticon.setTag(EmojiManager.getDisplayText(i));
            getAdapter().add(emoticon);
        }
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
        return adapter;
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new GridLayoutManager(getContext(), 7, LinearLayoutManager.VERTICAL, false);
    }
}
