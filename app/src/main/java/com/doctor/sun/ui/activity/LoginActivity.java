package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityLoginBinding;
import com.doctor.sun.event.UpdateEvent;
import com.doctor.sun.ui.handler.LoginHandler;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;


/**
 * Created by rick on 10/23/15.
 */
public class LoginActivity extends BaseFragmentActivity2 {

    private ActivityLoginBinding binding;
    private LoginHandler handler;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        handler = new LoginHandler();
        binding.setHandler(handler);

    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        if (shouldCheck()) {
//            UpdateUtil.checkUpdate(this);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        UpdateUtil.onPause();
//    }
//
//    @Subscribe
//    public void onUpdateEvent(UpdateEvent e) {
//        UpdateUtil.handleNewVersion(this, e.getData(), e.getVersionName());
//    }
}
