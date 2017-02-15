package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.ui.activity.BaseFragmentActivity2;

/**
 * Created by rick on 15/2/2017.
 */
public class MyIncomeActivity extends BaseFragmentActivity2 {

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, MyIncomeActivity.class);
        return intent;
    }

}
