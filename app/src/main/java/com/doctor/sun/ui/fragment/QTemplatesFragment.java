package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.QTemplate2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.List;

/**
 * Created by rick on 5/9/2016.
 */

public class QTemplatesFragment extends RefreshListFragment {
    public static final String TAG = QTemplatesFragment.class.getSimpleName();

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
        questionModule.templates2().enqueue(new SimpleCallback<List<QTemplate2>>() {
            @Override
            protected void handleResponse(List<QTemplate2> response) {
                if (response == null || response.isEmpty()) {
                    refreshEmptyIndicator();
                    binding.swipeRefresh.setRefreshing(false);
                    return;
                }
                getAdapter().clear();
                getAdapter().insertAll(response);
                getAdapter().notifyDataSetChanged();
                ItemTextInput2 ps = new ItemTextInput2(R.layout.item_r_red_text, "* 如需自定义模版和题目,请登录PC端账号。", "");
                ps.setTitleGravity(Gravity.START);
                getAdapter().insert(ps);
                refreshEmptyIndicator();
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
