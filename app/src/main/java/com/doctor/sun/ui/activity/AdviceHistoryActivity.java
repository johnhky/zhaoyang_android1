package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.ProfileModule;

/**
 * Created by rick on 16/8/2016.
 */

public class AdviceHistoryActivity extends PageActivity2 {

    ProfileModule api = Api.of(ProfileModule.class);

    @Override
    protected void loadMore() {
        super.loadMore();
        api.advice().enqueue(getCallback());
    }

    public static Intent intentFor(Context context) {
        return new Intent(context, AdviceHistoryActivity.class);
    }

    public static void startForm(Context context) {
        context.startActivity(intentFor(context));
    }

    public int getMidTitle() {
        return R.string.title_advice_history;
    }
}
