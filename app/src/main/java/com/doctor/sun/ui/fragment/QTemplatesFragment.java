package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.QTemplate2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;

import java.util.List;

/**
 * Created by rick on 5/9/2016.
 */

public class QTemplatesFragment extends RefreshListFragment {
    public static final String TAG = QTemplatesFragment.class.getSimpleName();

    QuestionModule questionModule = Api.of(QuestionModule.class);


    public static void startFrom(Context context) {
        context.startActivity(intentFor(context));
    }

    public static Intent intentFor(Context context) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_NAME, TAG);
        i.putExtra(Constants.FRAGMENT_TITLE, "问诊模版");
        return i;
    }


    @Override
    protected void loadMore() {
        super.loadMore();
        showEmptyIndicator();
        questionModule.templates2().enqueue(new SimpleCallback<List<QTemplate2>>() {
            @Override
            protected void handleResponse(List<QTemplate2> response) {
                getAdapter().clear();
                getAdapter().insertAll(response);
                getAdapter().notifyDataSetChanged();
                if (getAdapter().isEmpty()) {
                    showEmptyIndicator();
                }
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "您没有任何问诊模版";
    }
}
