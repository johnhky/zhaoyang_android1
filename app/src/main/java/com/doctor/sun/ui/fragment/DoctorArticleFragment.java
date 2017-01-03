package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Article;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 1/4/2016.
 */
@Factory(type = BaseFragment.class, id = "DoctorArticleFragment")
public class DoctorArticleFragment extends ListFragment {
    public static final String TAG = DoctorArticleFragment.class.getSimpleName();
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Bundle getArgs(int doctorId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.DATA, doctorId);

        return bundle;
    }

    public static DoctorArticleFragment getInstance(int doctorId) {
        DoctorArticleFragment instance = new DoctorArticleFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.DATA, doctorId);
        instance.setArguments(args);
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
