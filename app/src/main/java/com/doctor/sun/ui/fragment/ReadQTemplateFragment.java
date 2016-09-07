package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.QuestionsPath;

/**
 * Created by rick on 9/8/2016.
 */
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



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }
}
