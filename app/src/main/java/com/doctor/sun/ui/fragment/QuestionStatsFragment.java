package com.doctor.sun.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.ScalesResult;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;

import java.util.List;

import retrofit2.Call;

/**
 * Created by rick on 9/8/2016.
 */
@Factory(type = BaseFragment.class, id = "QuestionStatsFragment")
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

            @Override
            public void onFailure(Call<ApiDTO<List<ScalesResult>>> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
