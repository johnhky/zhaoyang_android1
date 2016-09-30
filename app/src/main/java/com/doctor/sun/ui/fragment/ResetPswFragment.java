package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.CountDownUtil;
import com.doctor.sun.util.MD5;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemCaptchaInput;
import com.doctor.sun.vo.ItemTextInput2;
import com.doctor.sun.vo.validator.RegexValidator;
import com.doctor.sun.vo.validator.Validator;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import io.ganguo.library.core.event.extend.OnSingleClickListener;

/**
 * Created by rick on 19/8/2016.
 */

public class ResetPswFragment extends SortedListFragment {
    public static final String TAG = ResetPswFragment.class.getSimpleName();

    public static void startFrom(Context context) {
        Intent intent = SingleFragmentActivity.intentFor(context, "忘记密码", getArgs());
        context.startActivity(intent);
    }

    public static Bundle getArgs() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        return bundle;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        disableRefresh();
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<BaseItem> sortedItems = new ArrayList<>();


        final ItemTextInput2 newPhoneNum = ItemTextInput2.mobilePhoneInput("手机号码", "请输入11位手机号码");
        newPhoneNum.setResultNotEmpty();
        newPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        newPhoneNum.setItemId("phone");
        sortedItems.add(newPhoneNum);

        insertDivider(sortedItems);

        final ItemCaptchaInput captcha = new ItemCaptchaInput(R.layout.item_input_captcha, "验证码", "请输入验证码");
        captcha.setResultNotEmpty();
        captcha.setListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(final View v) {
                if (!newPhoneNum.isValid("")) {
                    Toast.makeText(v.getContext(), newPhoneNum.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthModule profileModule = Api.of(AuthModule.class);

                profileModule.sendCaptcha(newPhoneNum.getResult()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        CountDownUtil.countDown((TextView) v, "重新获取(%d)", "获取验证码", 60);
                    }
                });
            }
        });
        captcha.setSubTitle("获取验证码");
        captcha.add(new RegexValidator(Pattern.compile("^\\s*(?:\\S\\s*){6,}$"), "请输入6位字符串验证码"));
        captcha.setMaxLength(6);
        captcha.setItemId("captcha");
        sortedItems.add(captcha);

        insertDivider(sortedItems);

        final ItemTextInput2 passwordOne = ItemTextInput2.password("设置密码", "请输入6位字符串密码");
        passwordOne.setResultNotEmpty();
        passwordOne.setItemLayoutId(R.layout.item_text_input2);
        passwordOne.setItemId("password");
        sortedItems.add(passwordOne);

        insertDivider(sortedItems);

        ItemTextInput2 passwordTwo = ItemTextInput2.password("重输密码", "请输入6位字符串密码");
        passwordTwo.setResultNotEmpty();
        passwordTwo.setItemLayoutId(R.layout.item_text_input2);
        passwordTwo.setItemId(UUID.randomUUID().toString());
        passwordTwo.add(new Validator() {
            @Override
            public boolean isValid(String input) {
                return passwordOne.getValue().equals(input);
            }

            @Override
            public String errorMsg() {
                return "密码跟第一个密码不相同";
            }
        });
        sortedItems.add(passwordTwo);

        insertDivider(sortedItems);

        for (int i = 0; i < sortedItems.size(); i++) {
            sortedItems.get(i).setPosition(i);
        }

        getAdapter().insertAll(sortedItems);
        binding.swipeRefresh.setRefreshing(false);
    }

    private void insertDivider(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_start13_end13);
        item.setItemId(UUID.randomUUID().toString());
        list.add(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_next, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next: {
                done();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        AuthModule api = Api.of(AuthModule.class);
        api.reset(toHashMap(getAdapter())).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(getContext(), "重置密码成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
    }


    private HashMap<String, String> toHashMap(SortedListAdapter adapter) {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < adapter.size(); i++) {
            BaseItem item = (BaseItem) adapter.get(i);

            if (!item.isValid("")) {
                if (!item.resultCanEmpty()) {
                    item.addNotNullOrEmptyValidator();
                }
                Toast.makeText(getContext(), item.errorMsg(), Toast.LENGTH_SHORT).show();
                return null;
            }

            String value = item.getValue();
            if (!Strings.isNullOrEmpty(value)) {
                String key = item.getKey();
                if (!Strings.isNullOrEmpty(key)) {
                    if (key.equals("password")) {
                        result.put(key, MD5.getMessageDigest(value.getBytes()));
                    } else {
                        result.put(key, value);
                    }
                }
            }
        }
        return result;
    }

}
