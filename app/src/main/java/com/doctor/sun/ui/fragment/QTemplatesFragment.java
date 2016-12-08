package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.doctor.auto.Factory;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.vm.ItemTextInput2;

/**
 * Created by rick on 5/9/2016.
 */

@Factory(type = BaseFragment.class, id = "QTemplatesFragment")
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
        questionModule.myTemplates(getPageCallback().getPage()).enqueue(getPageCallback());
    }


    @Override
    protected void insertFooter() {
        ItemTextInput2 ps = new ItemTextInput2(R.layout.item_r_red_text, "* 如需自定义模版和题目,请登录PC端账号。", "");
        ps.setTitleGravity(Gravity.START);
        getAdapter().add(ps);
    }

    @NonNull
    @Override
    protected String getEmptyIndicatorText() {
        return "您没有任何问诊模版";
    }
}
