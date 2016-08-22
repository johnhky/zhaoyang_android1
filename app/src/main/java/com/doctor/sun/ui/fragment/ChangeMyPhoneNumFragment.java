package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.util.CountDownUtil;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.ItemCaptchaInput;
import com.doctor.sun.vo.ItemTextInput2;
import com.doctor.sun.vo.validator.RegexValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import io.ganguo.library.core.event.extend.OnSingleClickListener;

/**
 * Created by rick on 19/8/2016.
 */

public class ChangeMyPhoneNumFragment extends SortedListFragment {
    public static final String TAG = ChangeMyPhoneNumFragment.class.getSimpleName();

    public static void startFrom(Context context) {
        context.startActivity(intentFor(context));
    }

    public static Intent intentFor(Context context) {
        Intent i = new Intent(context, SingleFragmentActivity.class);
        i.putExtra(Constants.FRAGMENT_NAME, TAG);
        return i;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        List<BaseItem> sortedItems = new ArrayList<>();
        final ItemTextInput2 oldPhoneNum = ItemTextInput2.phoneInput("旧的手机号码", "请输入11位手机号码");
        oldPhoneNum.setShouldNotBeEmpty(true);
        oldPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        oldPhoneNum.setItemId(UUID.randomUUID().toString());
        sortedItems.add(oldPhoneNum);

        insertDivider(sortedItems);

        final ItemTextInput2 newPhoneNum = ItemTextInput2.phoneInput("新的手机号码", "请输入11位手机号码");
        newPhoneNum.setShouldNotBeEmpty(true);
        newPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        newPhoneNum.setItemId(UUID.randomUUID().toString());
        sortedItems.add(newPhoneNum);

        insertDivider(sortedItems);

        final ItemCaptchaInput captcha = new ItemCaptchaInput(R.layout.item_input_captcha, "验证码", "请输入验证码");
        captcha.setShouldNotBeEmpty(true);
        captcha.setListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(final View v) {
                if (!newPhoneNum.isValid("")) {
                    Toast.makeText(v.getContext(), newPhoneNum.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthModule profileModule = Api.of(AuthModule.class);

                profileModule.sendCaptcha(newPhoneNum.getResult(), "change_phone").enqueue(new SimpleCallback<String>() {
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
        captcha.setItemId(UUID.randomUUID().toString());
        sortedItems.add(captcha);

        insertDivider(sortedItems);

        insertSpace(sortedItems);

        final ClickMenu clickMenu = new ClickMenu(R.layout.item_big_button2, -1, "完成", null);
        clickMenu.setShouldNotBeEmpty(false);
        clickMenu.setItemId(UUID.randomUUID().toString());
        clickMenu.add(oldPhoneNum);
        clickMenu.add(newPhoneNum);
        clickMenu.add(captcha);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clickMenu.isValid("")) {
                    Toast.makeText(view.getContext(), clickMenu.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthModule profileModule = Api.of(AuthModule.class);

                profileModule.changePhone(captcha.getValue(), newPhoneNum.getResult()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                    }
                });
            }
        };
        clickMenu.setListener(listener);
        sortedItems.add(clickMenu);


        for (int i = 0; i < sortedItems.size(); i++) {
            sortedItems.get(i).setPosition(i);
        }

        getAdapter().insertAll(sortedItems);
    }

    private void insertDivider(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_marginlr_13dp);
        item.setItemId(UUID.randomUUID().toString());
        list.add(item);
    }

    private void insertSpace(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.space_30dp);
        item.setItemId(UUID.randomUUID().toString());
        list.add(item);
    }
}
