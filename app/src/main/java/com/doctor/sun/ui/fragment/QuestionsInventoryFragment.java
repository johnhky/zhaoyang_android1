package com.doctor.sun.ui.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentQuestionsInventoryBinding;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemCustomQuestionLoader;
import com.doctor.sun.vo.ItemSearch;
import com.doctor.sun.vo.ItemSystemQuestionLoader;
import com.google.common.base.Strings;

/**
 * Created by rick on 9/9/2016.
 */

public class QuestionsInventoryFragment extends SortedListFragment {
    public static final String TAG = QuestionsInventoryFragment.class.getSimpleName();
    private FragmentQuestionsInventoryBinding flBinding;
    private ItemSearch searchItem;
    private String keyword;


    public static Bundle getArgs(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        flBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions_inventory, binding.flRoot, true);
        searchItem = new ItemSearch();
        flBinding.setSearchItem(searchItem);
        listenerKeywordChange();
        return view;
    }

    public void listenerKeywordChange() {
        if (searchItem == null) {
            searchItem = new ItemSearch();
        }
        searchItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.keyword) {
                    if (Strings.isNullOrEmpty(searchItem.getKeyword())) {
                        if (Strings.isNullOrEmpty(keyword)) {
                            return;
                        }
                        keyword = searchItem.getKeyword();
                        getAdapter().clear();
                        loadMore();
                    }
                } else if (i == BR.submit) {
                    keyword = searchItem.getKeyword();
                    getAdapter().clear();
                    loadMore();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadMore();
    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
        adapter.putString(AdapterConfigKey.ID, getAppointmentId());
        adapter.setLayoutIdInterceptor(new BaseListAdapter.LayoutIdInterceptor() {
            @Override
            public int intercept(int origin) {
                switch (origin) {
                    case R.layout.item_question: {
                        return R.layout.item_inventory_question;
                    }
                    case R.layout.item_further_consultation: {
                        return R.layout.item_empty;
                    }
                }
                return origin;
            }
        });
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        ItemSystemQuestionLoader loader = new ItemSystemQuestionLoader(R.layout.item_system_question_title, getAppointmentId(), keyword);
        //放到第一位 ,问题的position 是从0开始,所以这里给个负数
        loader.setPosition(-1000);
        loader.setTitle("系统题目");
        getAdapter().insert(loader);

        ItemCustomQuestionLoader loader2 = new ItemCustomQuestionLoader(R.layout.item_custom_question_title, getAppointmentId(),keyword);
        //放到最后一位
        loader2.setPosition(4999 * QuestionsModel.PADDING);
        loader2.setTitle("自定义题目");
        getAdapter().insert(loader2);


        flBinding.setAdapter(getAdapter());
        flBinding.setSystemQuestions(loader);
        flBinding.setCustomQuestions(loader2);
        loader.expend(getAdapter());
        binding.swipeRefresh.setRefreshing(false);
    }


    private String getAppointmentId() {
        return getArguments().getString(Constants.DATA);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_hide_options, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide_options: {
                hideOptions();
                break;
            }
            case R.id.action_show_options: {
                showOptions();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showOptions() {
        for (int i = 0; i < getAdapter().size(); i++) {
            BaseItem item = (BaseItem) getAdapter().get(i);
            setVisible(item);
        }
        getAdapter().notifyDataSetChanged();
    }


    public void hideOptions() {
        for (int i = 0; i < getAdapter().size(); i++) {
            BaseItem item = (BaseItem) getAdapter().get(i);
            setInVisible(item);
        }

        getAdapter().notifyDataSetChanged();
    }

    public void setInVisible(BaseItem item) {
        if (item.getLayoutId() != R.layout.item_inventory_question) {
            if (item.getLayoutId() != R.layout.divider_1px_top13) {
                item.setVisible(false);
            }
        }
    }

    public void setVisible(BaseItem item) {
        if (item.getLayoutId() != R.layout.item_inventory_question) {
            if (item.getLayoutId() != R.layout.divider_1px_top13) {
                item.setVisible(true);
            }
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(false);
    }
}
