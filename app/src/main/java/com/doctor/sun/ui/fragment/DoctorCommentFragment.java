package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 1/4/2016.
 */
@Factory(type = BaseFragment.class, id = "DoctorCommentFragment")
public class DoctorCommentFragment extends RefreshListFragment {
    public static final String TAG = DoctorCommentFragment.class.getSimpleName();
    private ProfileModule api = Api.of(ProfileModule.class);

    public static Bundle getArgs(int doctorId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putInt(Constants.DATA, doctorId);

        return bundle;
    }

    public static DoctorCommentFragment getInstance(int doctorId) {
        DoctorCommentFragment instance = new DoctorCommentFragment();
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
        super.loadMore();
        api.comments(getDoctorId(), getPageCallback().getPage()).enqueue(getPageCallback());
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "还没有任何人对该医生进行过评论";
    }
}
