package io.ganguo.library.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.ganguo.library.R;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Fragment - 基类
 * <p/>
 * Created by zhihui_chen on 14-8-4.
 */
public abstract class BaseFragment extends Fragment implements InitResources {

    private final Logger logger = LoggerFactory.getLogger(BaseFragment.class);

    public String getFragmentTitle() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logger.d("onCreateView");
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // register event
        EventHub.register(this);
        // bind back action
        UIHelper.bindActionBack(getActivity(), getView().findViewById(R.id.action_back));

        // init resources
        initView();
        initListener();
        initData();

        logger.d("onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // unregister event
        EventHub.unregister(this);

        logger.i("onDestroy");
    }

}
