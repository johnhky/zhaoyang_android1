package com.doctor.sun.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.ScalesResult;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;

import java.util.List;

/**
 * Created by rick on 9/8/2016.
 */
public class QuestionStatsFragment extends RefreshListFragment {
    public static final String TAG = QuestionStatsFragment.class.getSimpleName();

    private QuestionModule api = Api.of(QuestionModule.class);

    public static Bundle getArgs(String id, String path) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.PATH, path);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setBackgroundColor(Color.parseColor("#ebf0f3"));
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        Bundle arguments = getArguments();
        String string = arguments.getString(Constants.PATH);
        String id = arguments.getString(Constants.DATA);
        api.questionResult(string, id).enqueue(new SimpleCallback<List<ScalesResult>>() {
            @Override
            protected void handleResponse(List<ScalesResult> response) {
                getAdapter().addAll(response);
                getAdapter().notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}
