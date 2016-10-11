package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 1/4/2016.
 */
public class DoctorCommentFragment extends ListFragment {
    private ProfileModule api = Api.of(ProfileModule.class);


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
        PageCallback<Comment> callback = new PageCallback<>(getAdapter());
        api.comments(getDoctorId(), callback.getPage()).enqueue(callback);
    }
}
