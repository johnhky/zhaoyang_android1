package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.auto.Factory;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.model.ResetPasswordModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;

import java.util.List;

/**
 * Created by heky on 17/6/26.
 */
@Factory(type = BaseFragment.class, id = "SetPasswordFragment")
public class SetPasswordFragment extends SortedListFragment {
    public static final String TAG = SetPasswordFragment.class.getSimpleName();
    private ResetPasswordModel model;
    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ResetPasswordModel();
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<SortedItem>result = model.parseData(getActivity(),getAdapter());
        getAdapter().insertAll(result);
        binding.swipeRefresh.setRefreshing(false);
    }
}
