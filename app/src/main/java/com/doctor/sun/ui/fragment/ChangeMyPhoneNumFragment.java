package com.doctor.sun.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.ItemTextInput2;
import com.doctor.sun.vo.validator.RegexValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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
        oldPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        oldPhoneNum.setItemId(UUID.randomUUID().toString());
        sortedItems.add(oldPhoneNum);

        insertDivider(sortedItems);

        ItemTextInput2 newPhoneNum = ItemTextInput2.phoneInput("新的手机号码", "请输入11位手机号码");
        newPhoneNum.setItemLayoutId(R.layout.item_text_input2);
        newPhoneNum.setItemId(UUID.randomUUID().toString());
        sortedItems.add(newPhoneNum);

        insertDivider(sortedItems);

        ItemTextInput2 captcha = new ItemTextInput2(R.layout.item_input_captcha, "验证码", "");
        captcha.setSubTitle("");
        captcha.add(new RegexValidator(Pattern.compile("^\\s*(?:\\S\\s*){6,}$"), "请输入6位字符串验证码"));
        captcha.setMaxLength(6);
        captcha.setItemId(UUID.randomUUID().toString());
        sortedItems.add(captcha);

        insertDivider(sortedItems);

        for (int i = 0; i < sortedItems.size(); i++) {
            sortedItems.get(i).setPosition(i);
        }

        getAdapter().insertAll(sortedItems);
    }

    private void insertDivider(List<BaseItem> list) {
        BaseItem item = new BaseItem(R.layout.divider_1px_marginlr_13dp);
        item.setItemId(UUID.randomUUID().toString());
//        int size = list.size();
//        item.setPosition(size);
        list.add(item);
    }
}
