package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;

/**
 * Created by rick on 9/8/2016.
 */
@Factory(type = BaseFragment.class, id = "ReadQTemplateFragment")
public class ReadQTemplateFragment extends AnswerQuestionFragment {
    public static final String TAG = ReadQTemplateFragment.class.getSimpleName();


    public static ReadQTemplateFragment getInstance(String id, String path, boolean readOnly) {
        return getInstance(id, path, "", readOnly);
    }

    public static ReadQTemplateFragment getInstance(String id, @QuestionsPath String path, String questionType, boolean readOnly) {

        ReadQTemplateFragment fragment = new ReadQTemplateFragment();

        Bundle args = getArgs(id, path, questionType, readOnly);

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    public static Bundle getArgs(String id, @QuestionsPath String path, String questionType, boolean readOnly) {
        Bundle args = new Bundle();
        args.putString(Constants.FRAGMENT_NAME, TAG);
        args.putString(Constants.DATA, id);
        args.putString(Constants.PATH, path);
        args.putString(Constants.QUESTION_TYPE, questionType);
        args.putBoolean(Constants.READ_ONLY, readOnly);
        return args;
    }

    public String getType() {
        return getArguments().getString(Constants.PATH);
    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
        adapter.setLayoutIdInterceptor(new SortedListAdapter.LayoutIdInterceptor() {
            @Override
            public int intercept(int origin) {
                if (origin == R.layout.item_scales) {
                    return R.layout.item_r_template_scales;
                }
                return origin;
            }
        });
        return adapter;
    }


    /**
     * 去掉保存问卷按钮
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }
}
