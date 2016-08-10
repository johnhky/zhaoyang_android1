package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.Function0;
import com.doctor.sun.vo.ItemPickImage;

import java.util.List;

import io.ganguo.library.Config;

/**
 * Created by rick on 28/7/2016.
 */

public class AnswerQuestionFragment extends SortedListFragment {

    private int appointmentId;
    private QuestionsModel model;

    public static AnswerQuestionFragment getInstance(int appointmentId) {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(Constants.DATA, appointmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appointmentId = 138;
//        appointmentId = getArguments().getInt(Constants.DATA);
        model = new QuestionsModel();
        setHasOptionsMenu(true);
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        final String string = Config.getString(Constants.TOKEN);
        Config.putString(Constants.TOKEN, "5d8f8d7946a49e696f4b298666762723");
        model.questions(appointmentId, new Function0<List<? extends SortedItem>>() {
            @Override
            public void apply(List<? extends SortedItem> sortedItems) {
                getAdapter().insertAll(sortedItems);
                binding.swipeRefresh.setRefreshing(false);
                Config.putString(Constants.TOKEN, string);
            }
        });
//        JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, Questions2.class);
//        List<Questions2> questions = JacksonUtils.fromResource(getContext(), R.raw.json, type);
//        List<SortedItem> sortedItems = model.parseQuestions(questions);
//        getAdapter().insertAll(sortedItems);
//        binding.swipeRefresh.setRefreshing(false);
    }

    public void save() {
        model.saveAnswer(appointmentId, getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                save();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ItemPickImage.handleRequest(getActivity(), getAdapter(), data, requestCode);
        }
    }

}
