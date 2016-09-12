package com.doctor.sun.ui.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentQuestionsInventoryBinding;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.vo.ItemCustomQuestionLoader;
import com.doctor.sun.vo.ItemSystemQuestionLoader;

/**
 * Created by rick on 9/9/2016.
 */

public class QuestionsInventoryFragment extends SortedListFragment {
    public static final String TAG = QuestionsInventoryFragment.class.getSimpleName();
    private FragmentQuestionsInventoryBinding flBinding;

    private ObservableInt currentSelectedTab = new ObservableInt();

    public static Bundle getArgs(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        flBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions_inventory, binding.flRoot, true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        ItemSystemQuestionLoader loader = new ItemSystemQuestionLoader(R.layout.item_system_question_title, getAppointmentId());
        //放到第一位 ,问题的position 是从0开始,所以这里给个负数
        loader.setPosition(-1000);
        loader.setTitle("系统题目");
        getAdapter().insert(loader);

        ItemCustomQuestionLoader loader2 = new ItemCustomQuestionLoader(R.layout.item_custom_question_title, getAppointmentId());
        //放到最后一位
        loader2.setPosition(4999 * QuestionsModel.PADDING);
        loader2.setTitle("自定义题目");
        getAdapter().insert(loader2);


        flBinding.setAdapter(getAdapter());
        flBinding.setSystemQuestions(loader);
        flBinding.setCustomQuestions(loader2);
        binding.swipeRefresh.setRefreshing(false);
    }


    private String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }
}
