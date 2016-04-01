package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Article;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 1/4/2016.
 */
public class DoctorArticleFragment extends ListFragment {
    private ProfileModule api = Api.of(ProfileModule.class);


    private static DoctorArticleFragment instance;

    public static DoctorArticleFragment getInstance(int doctorId) {
        if (instance == null) {
            instance = new DoctorArticleFragment();
            Bundle args = new Bundle();
            args.putInt(Constants.DATA, doctorId);
            instance.setArguments(args);
        }
        return instance;
    }

    private int getDoctorId() {
        return getArguments().getInt(Constants.DATA);
    }

    @Override
    protected void loadMore() {
        PageCallback<Article> callback = new PageCallback<Article>(getAdapter());
        api.articles(getDoctorId(), callback.getPage()).enqueue(callback);
    }
}
