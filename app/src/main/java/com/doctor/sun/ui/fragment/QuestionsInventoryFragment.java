package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.Try;
import com.doctor.sun.vo.ItemLoadMore;

import java.util.List;

/**
 * Created by rick on 9/9/2016.
 */

public class QuestionsInventoryFragment extends SortedListFragment {
    public static final String TAG = QuestionsInventoryFragment.class.getSimpleName();

    QuestionModule questionModule = Api.of(QuestionModule.class);

    QuestionsModel questionsModel;
    int systemQuestionsPage = 1;
    int systemQuestionsCount = 0;
    List<SortedItem> customQuestions;

    public static Bundle getArgs(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionsModel = new QuestionsModel();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        questionModule.systemQuestions(getAppointmentId(), String.valueOf(systemQuestionsPage)).enqueue(new SimpleCallback<PageDTO<Questions2>>() {
            @Override
            protected void handleResponse(PageDTO<Questions2> response) {
                List<Questions2> data = response.getData();
                if (data != null && !data.isEmpty()) {
                    List<SortedItem> items = questionsModel.parseQuestions(data, systemQuestionsCount);

                    getAdapter().insertAll(items);
                    systemQuestionsCount += data.size();
                    systemQuestionsPage += 1;
                }
                int to = response.getTo();
                int total = response.getTotal();
                int perPage = response.getPerPage();
                boolean finished = to >= total || (systemQuestionsPage - 1) * perPage >= total;
                if (!finished) {
                    insertLoadMore();
                } else {
                    getAdapter().removeItem(new ItemLoadMore());
                }

                binding.swipeRefresh.setRefreshing(false);
            }
        });
//        questionModule.customQuestions(getAppointmentId()).enqueue(new SimpleCallback<List<Questions2>>() {
//            @Override
//            protected void handleResponse(List<Questions2> response) {
//                customQuestions = questionsModel.parseQuestions(response, 0);
//                getAdapter().insertAll(customQuestions);
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    private void insertLoadMore() {
        final ItemLoadMore item = new ItemLoadMore();
        item.setLoadMoreListener(new Try() {
            @Override
            public void success() {
                loadMore();
            }

            @Override
            public void fail() {
            }
        });
        getAdapter().insert(item);
    }


    private String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }
}
