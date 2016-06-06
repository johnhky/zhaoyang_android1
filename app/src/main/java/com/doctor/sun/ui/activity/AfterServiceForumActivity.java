package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.vo.ItemPickDate;

import java.util.List;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceForumActivity extends PageActivity2 {
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private AnswerModifyAdapter adapter;


    public static Intent intentFor(Context context, String id) {
        Intent intent = new Intent(context, AfterServiceForumActivity.class);
        intent.putExtra(Constants.DATA, id);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getData();
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("查看问卷");
        header.setRightTitle("保存问卷");
        getBinding().setHeader(header);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new AnswerModifyAdapter(this);
        adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service_detail);
        adapter.mapLayout(R.layout.item_pick_date, R.layout.item_pick_question_date);
//        adapter.mapLayout(R.layout.item_answer, R.layout.item_answer3);
        return adapter;
    }

    @Override
    protected void loadMore() {
        String type = "4";
        if (AppContext.isDoctor()) {
            type = "3";
        }

        api.questions(orderId, type).enqueue(new SimpleCallback<AfterServiceDTO>() {
            @Override
            protected void handleResponse(AfterServiceDTO response) {
                getAdapter().clear();
                getAdapter().onFinishLoadMore(true);
                getAdapter().addAll(response.followUpInfo);
                for (int i = 0; i < response.questions.size(); i++) {
                    Answer answer = response.questions.get(i);
                    answer.setPosition(i + 1);
                    answer.setEditMode(true);
                    getAdapter().add(answer);
                    switch (answer.getQuestion().getQuestionType()) {
                        case Question.TYPE_CHECKBOX:
                        case Question.TYPE_RADIO:
                            List<Options> options = answer.getQuestion().getOptions();
                            for (Options option : options) {
                                option.setParentPosition(getAdapter().size() - 1);
                            }
                            getAdapter().addAll(options);
                            break;
                        case Question.TYPE_TIME: {
                            getAdapter().add(new ItemPickDate(R.layout.item_pick_question_date, "", 0));
                            break;
                        }
                        case Question.TYPE_DROP_DOWN: {
                            getAdapter().add(new ItemSelectHospital());
                        }
                    }
                }

                getAdapter().notifyDataSetChanged();
                getBinding().refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        String answer = adapter.toJsonAnswer();
        Log.e(TAG, "onMenuClicked: " + answer);
        api.saveAnswer(orderId, answer).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(AfterServiceForumActivity.this, "成功保存问卷", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
