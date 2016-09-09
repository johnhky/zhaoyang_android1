package com.doctor.sun.ui.fragment;

import android.os.Bundle;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.QuestionModule;

/**
 * Created by rick on 9/9/2016.
 */

public class MyScalesInventoryFragment extends RefreshListFragment {
    public static final String TAG = MyScalesInventoryFragment.class.getSimpleName();

    QuestionModule questionModule = Api.of(QuestionModule.class);


    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        refreshEmptyIndicator();
        questionModule.myTemplates(getPageCallback().getPage()).enqueue(getPageCallback());
    }

}
