package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemSwitch;
import com.doctor.sun.vo.ItemTextInput2;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by rick on 28/7/2016.
 */

public class AllowToSearchFragment extends SortedListFragment {
    public static final String TAG = AllowToSearchFragment.class.getSimpleName();
    private ProfileModule profileModule = Api.of(ProfileModule.class);

    public static void startFrom(Context context) {
        context.startActivity(intentFor(context));
    }

    public static Intent intentFor(Context context) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_NAME, TAG);
        i.putExtra(Constants.FRAGMENT_TITLE, "我");
        return i;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        profileModule.isAllowToSearch().enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                ArrayList<BaseItem> items = new ArrayList();
                final ItemSwitch itemSwitch = new ItemSwitch(R.layout.item_switch, "是否允许公开检索权限");
                if ("1".equals(response)) {
                    itemSwitch.setChecked(true);
                } else {
                    itemSwitch.setChecked(false);
                }

                itemSwitch.setItemId(UUID.randomUUID().toString());
                itemSwitch.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        if (i == BR.isChecked) {
                            profileModule.toggleSearchable().enqueue(new SimpleCallback<String>() {
                                @Override
                                protected void handleResponse(String response) {
                                    Log.d(TAG, "handleResponse() called with: response = [" + response + "]");
                                }
                            });
                        }
                    }
                });
                items.add(itemSwitch);

                BaseItem baseItem = new BaseItem(R.layout.space_30dp);
                baseItem.setItemId(UUID.randomUUID().toString());

                items.add(baseItem);

                ItemTextInput2 textInput2 = new ItemTextInput2(R.layout.item_r_text_dp200, "", "");
                textInput2.setItemId(UUID.randomUUID().toString());
                textInput2.setTitle("* 说明 :  打开此权限公众端（患者和家属）可在APP上直接搜到您的个人信息，如您有信息保密的考虑，可关闭此权限。患者可通过扫码与您建立咨询和随访关系，您的信息仅在对方的手机上查看到。");
                items.add(textInput2);

                for (int i = 0; i < items.size(); i++) {
                    BaseItem baseItem1 = items.get(i);
                    baseItem1.setPosition(i);
                    getAdapter().insert(baseItem1);
                }
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getBinding().swipeRefresh.setRefreshing(false);
    }
}